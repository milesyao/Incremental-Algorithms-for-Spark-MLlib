/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.mllib.classification

import org.apache.spark.mllib.regression.LabeledPoint
import org.scalatest.FunSuite

import org.apache.spark.mllib.util.TestingUtils._
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.TestSuiteBase
import scala.util.Random
import org.apache.spark.SparkException


class StreamingNaiveBayesSuite extends FunSuite with TestSuiteBase {

  // use longer wait time to ensure job completion
  override def maxWaitTimeMillis = 30000

  test("accuracy") {

    //val lambda=1.0
    val nPoints = 1000
    val pi = Array(0.5, 0.3, 0.2)
    val theta = Array(
      Array(0.91, 0.03, 0.03, 0.03), // label 0
      Array(0.03, 0.91, 0.03, 0.03), // label 1
      Array(0.03, 0.03, 0.91, 0.03) // label 2
    )
    val decayFactor = 1.0
    val model = new StreamingNaiveBayes()

    // generate random data for k-means
    val testData = NaiveBayesSuite.generateNaiveBayesInput(pi, theta, nPoints, 42)
    val testRDD = sc.parallelize(testData, 5)
    testRDD.cache()
    // setup and run the model training
    val ssc = setupStreams(testRDD, (inputDStream: DStream[LabeledPoint]) => {
      model.trainOn(inputDStream)
      inputDStream.count()
    })

    runStreams(ssc, nPoints)

    val numOfPredictions1 = testData.map(row => model.predict(row.features)).zip(testData).count {
      case (prediction1, expected1) =>
        prediction1 != expected1.label
    }
    // At least 80% of the predictions should be on.
    assert(numOfPredictions1 < testData.length / 5)

    val numOfPredictions2 = model.predict(testRDD.map(_.features)).collect().zip(testRDD).count{
      case (prediction2, expected2) =>
      prediction2 != expected2.label
    }

    assert(numOfPredictions2 < testData.length / 5)

  }

  test("detect negative values"){
    val dense = Seq(
      LabeledPoint(1.0, Vectors.dense(1.0)),
      LabeledPoint(0.0, Vectors.dense(-1.0)),
      LabeledPoint(1.0, Vectors.dense(0.0)))
    intercept[SparkException] {
      StreamingNaiveBayes.train(sc.makeRDD(dense, 2))
    }

    val sparse = Seq(
      LabeledPoint(1.0, Vectors.sparse(1, Array(0), Array(1.0))),
      LabeledPoint(0.0, Vectors.sparse(1, Array(0), Array(-1.0))),
      LabeledPoint(1.0, Vectors.sparse(1, Array.empty, Array.empty)))
    intercept[SparkException] {
      StreamingNaiveBayes.train(sc.makeRDD(sparse, 2))
    }

    val nan = Seq(
      LabeledPoint(1.0, Vectors.sparse(1, Array(0), Array(1.0))),
      LabeledPoint(0.0, Vectors.sparse(1, Array(0), Array(Double.NaN))),
      LabeledPoint(1.0, Vectors.sparse(1, Array.empty, Array.empty)))
    intercept[SparkException] {
      StreamingNaiveBayes.train(sc.makeRDD(nan, 2))
    }
  }

 }








