# Incremental-Algorithms-for-Spark-MLlib
This project intends to add more incremental algorithm support for Spark MLlib, including Naive Bayes, Collaborative filtering, SVM, freqent pattern mining, etc.

## Compatibility

This framwork should be worked on with Spark 1.3.0 and Scala 2.10.4, JDK 7+. 

## Develop Requirements

Our first work should be Streaming Naive Bayes. We are expected to fullfill the following things:

1. Write streaming Naive Bayes algorithm, referring to original Naive Bayes implementation and streaming algorithm implementation. 

2. Write streaming naive bayes test suite. Prove step 1's correctness and functionality. Please refer to the ["ScalaTest"](http://http://www.scalatest.org/) for more information. 

## The Coding Template

This is a coding template incorporating SBT settings and typical Spark MLlib re-development framework. However, function entries are undefined, which are the developer's duty to fill them up, according to their own understanding to streaming algorithms. 

Please refer to Spark MLlib 1.3.0 source code (Streaming LR, Streaming K-means, especially) to implement Streaming Naive Bayes.

## Import project

In Intellij IDEA(14, maybe similar for 13), click File-Import Project. Find the root of this framwork, click next. Choose "Import project from external model", then choose SBT. Click next. Choose "User auto-import" and Project SDK(1.7.0 or above). Click finish. 


