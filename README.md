# Learning for Online Query Relaxation
An implementation of [Muslea's LOQR algorithm](http://webpages.uncc.edu/ras/Muslea-paper.pdf) (A class project)

## Running the Project
- Download and unpack [Gradle](https://services.gradle.org/distributions/gradle-2.3-all.zip) (for dependencies)
- Download and unpack [the zipfile containing the latest source](https://github.com/SeanTater/loqr/archive/master.zip).
- Open a terminal, cd to the loqr repository (not gradle)
- Execute `path/to/gradle/bin/gradle run`, changing the path as necessary so that you call the gradle binary
 - It's possible to use other tables, but the simplest way (using gradle) assumes the table is `diabetes.arff`.

## Target Data
This program was developed using the [Pima Indians Diabetes dataset](https://archive.ics.uci.edu/ml/datasets/Pima+Indians+Diabetes) as its target but it can be run on other ARFF files as well, with mixed discrete and continuous attributes. Continuous attributes are not discretized except for the rule-generation process, which is not visible to the user. So to write queries, you do not need to know the cuts. The columns for that dataset are as follows:

|idx| name  | mean | stdev | meaning
|---|-------|------|-------|---------
|  0| preg  | 3.8  | 3.4   | Number of times pregnant
|  1| plas  | 120.9| 32.0  | Plasma glucose concentration a 2 hours in an oral glucose tolerance test
|  2| pres  | 69.1 | 19.4  | Diastolic blood pressure (mm Hg)
|  3| skin  | 20.5 | 16.0  | Triceps skin fold thickness (mm)
|  4| insu  | 79.8 | 115.2 | 2-Hour serum insulin (mu U/ml)
|  5| mass  | 32.0 | 7.9   | Body mass index (weight in kg/(height in m)^2)
|  6| pedi  | 0.5  | 0.3   | Diabetes pedigree function
|  7| age   | 33.2 | 11.8  | Age (years)
|  8| class |      |       | Test results for Diabates: Class variable

## Query format
The LOQR system expects queries to have a specific format, as given in the example for the assignment. It consists of a conjunction of single rules of the form `[varname op value]`, separated by carats.

| Query Part | Type               | Examples
|------------|--------------------| ---
| varname    | attribute ([a-z]+) | `preg`, `plas`, `pres`, `skin`, `insu`, `mass`, `pedi`, or `age`
| op         | symbol             | `<`, `<=`, `==`, `!=`, `>=`, or `>`
| value      | numeric or string  | Continuous attributes: `3`, `-7`, `12.92`, otherwise any string without a ]
| conjunct   | rule               | `[preg <= 4]`, `[mass > 33]`
| query      | list of conjuncts  | `[preg <= 4]^[preg > 1]`, `[mass > 33]^[preg != 2]^[age < 55]`

# Development notes:
- [ID3 Reference](http://www.cis.temple.edu/~giorgio/cis587/readings/id3-c45.html)
- [Assignments](http://webpages.uncc.edu/ras/KBS-15.html)
- [Powerpoint Slide on Muslea's Algorithm](http://www.cs.uncc.edu/~ras/Failing-Queries-Muslea.ppt)
