# Learning for Online Query Relaxation
An implementation of [Muslea's LOQR algorithm](http://webpages.uncc.edu/ras/Muslea-paper.pdf) (A class project)

# Running the Project
- Download and unpack [Gradle](https://services.gradle.org/distributions/gradle-2.3-all.zip) (for dependencies)
- Download and unpack [the zipfile containing the latest source](https://github.com/SeanTater/loqr/archive/master.zip).
- Open a terminal, cd to the loqr repository (not gradle)
- Execute `path/to/gradle/bin/gradle run`, changing the path as necessary so that you call the gradle binary
 - It's possible to use other tables, but the simplest way (using gradle) assumes the table is `diabetes.arff`.
# Target Data
This program was developed for specific target data: the Pima Indians Diabetes Database, included as diabetes.arff in the root folder of the project.

# Development notes:
- [ID3 Reference](http://www.cis.temple.edu/~giorgio/cis587/readings/id3-c45.html)
- [Assignments](http://webpages.uncc.edu/ras/KBS-15.html)
- [Powerpoint Slide on Muslea's Algorithm](http://www.cs.uncc.edu/~ras/Failing-Queries-Muslea.ppt)
