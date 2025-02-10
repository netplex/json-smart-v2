# json-smart-v2
[![Build Status](https://travis-ci.org/netplex/json-smart-v2.svg?branch=master)](https://travis-ci.org/netplex/json-smart-v2)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.minidev/json-smart/badge.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/net.minidev/json-smart/)
[![Coverage Status](https://coveralls.io/repos/github/netplex/json-smart-v2/badge.svg?branch=master)](https://coveralls.io/github/netplex/json-smart-v2?branch=master)


Json-smart development started in 2010, when SQL servers did not support native JSON fields, NoSQL databases were slowly emerging, and all the existing JSON APIs were bogus. I wrote lots of tests to benchmark and compare JSON java parsers. 

I never liked SQL databases because it's almost impossible to update a data model without impacting the production platform. Adding a column is a terrible operation.

So I started json-smart. All non-indexed data in my datastores were stored in a column as a serialized JSON message. To fit MySQL varchar(255) fields, I tried to make my JSON as small as possible, so json-smart is optimized to produce small JSON-like Strings.

Now times have changed: most of the JSON APIs are now stable, and I'm now using document-oriented databases and JSON-native SQL types.

So I do not use my json-smart anymore. I had fun with this project. If you want to apply some change on json-smart create a pull request with lots of JUnit tests.


[WIKI is here](https://github.com/netplex/json-smart/wiki)

# Changelog

### *V 2.5.2* (next version)

* Fix CVE-2024-57699 for predefined parsers. [PR 233](https://github.com/netplex/json-smart-v2/pull/233)

### *V 2.5.1* (2024-03-14)

* Bump all dependencies.
* Fix OSGi import package version for net.minidev.asm. [PR 180](https://github.com/netplex/json-smart-v2/pull/180)

### *V 2.5.0* (2023-07-10)

* Add flag to drop the limit of json depth. [PR 156](https://github.com/netplex/json-smart-v2/pull/156)

### *V 2.4.11* (2023-05-18)

* Fix error in isWritable in accessor-smart. [PR 147](https://github.com/netplex/json-smart-v2/pull/147)
* Update json-smart dependency to use accessor-smart:2.4.11

### *V 2.4.10* (2023-03-17)

* Fix unstacking issue with more than 400 elements in an array.

### *V 2.4.9* (2023-03-07)

* Add depth limit of 400 when parsing JSON.

### *V 2.4.8* (2022-02-13)

* Fix the incorrect double compare with e,E+,e+ [PR 77](https://github.com/netplex/json-smart-v2/pull/77)

### *V 2.4.7* (2021-06-02)
* full timezone support in date parsing
* set default charset to UTF-8 when parsing byte[] contents
* overwride system default encoding when parssing bytes[] [PR 71](https://github.com/netplex/json-smart-v2/pull/74)

### *V 2.4.6* (2021-04-23)
* Correct publish issue 3th time [issue 69](https://github.com/netplex/json-smart-v2/issues/69)
* Support latest asm in accessor-smart [issue 70](https://github.com/netplex/json-smart-v2/issues/70)
* Drop legacy parent pom

### *V 2.4.5* (2021-04-19)
* Correct publish issue 2nd time [issue 69](https://github.com/netplex/json-smart-v2/issues/69)

### *V 2.4.4* (2021-04-17)
* Correct publish issue 1st time [issue 69](https://github.com/netplex/json-smart-v2/issues/69)
* fix ArrayIndexOutOfBoundsException [issue 68](https://github.com/netplex/json-smart-v2/pull/68)

### *V 2.4.2* (2021-04-04)

### *V 2.4.2* (2021-04-03)
* add BIG_DIGIT_UNRESTRICTED to avoid BigDigit usage on some Double.
* fix CVE-2021-27568
* java 11 build

### *V 2.3.1* (2021-05-02)
* Fixes [issue #60](https://github.com/netplex/json-smart-v2/issues/60) (CVE-2021-27568)
* full timezone support in date parsing

### *V 2.3* (2017-03-26)
* Patch 37 [issue 37](http://code.google.com/p/json-smart/issues/detail?id=37)
* Explicite support of char 127 [issue 18](http://code.google.com/p/json-smart/issues/detail?id=18)
* Integrate json-smart-action from Eitan Raviv [PR 31](https://github.com/netplex/json-smart-v2/pull/31)
* Remove hard codded e.printStackTrace() [issue 33](https://github.com/netplex/json-smart-v2/issues/33)
* Improve date parsing code to support all timeZones [issue 29](https://github.com/netplex/json-smart-v2/issues/29)

### *V 2.2.2*
 * Fix support for default java datetime format for US locale
 * Update my time Zone from Paris to San Francisco.

### *V 2.2.1* (2015-10-08)
* Fix issue in strict mode [issue gh-17](https://github.com/netplex/json-smart-v2/issues/17)
* Add a licence Copy at the root project level [issue gh-16](https://github.com/netplex/json-smart-v2/issues/16)
* Change InputStream input reading to use UTF8. [issue 48](http://code.google.com/p/json-smart/issues/detail?id=48)

### *V 2.2* (2015-07-29)
* Rename asm to accessors-smart due to conflict name with asm.ow2.org lib. fix [PR-10](https://github.com/netplex/json-smart-v2/pull/10)
* Fix OSGI error fix [PR-2](https://github.com/netplex/json-smart-v2/pull/2)
* Add support for BigDecimal
* Improve JSONObject.getAsNumber() helper
* Add a Field Remaper

### *V 2.1.0* (2014-10-19)
  * net.minidev.json.mapper renamed to net.minidev.json.writer
  * Add ACCEPT_TAILLING_SPACE Parssing Flag.
  * Mapper classes now non static.
  * Reader mapper are now available in net.minidev.json.reader.JsonReader class
  * Writer mapper are now available in net.minidev.json.writer.JsonWriter class

### *V 2.0* (2014-08-12)
  * Fix Double Identification [issue 44](http://code.google.com/p/json-smart/issues/detail?id=44)
  * Fix Collection Interface Serialisation
  * Fix security Exception in ASM code
  * Project moved to GitHub
  * Fix [issue 42](http://code.google.com/p/json-smart/issues/detail?id=42)

### *V 2.0-RC3* (2013-08-14)
  * Add custom data binding inside the ASM layer.
  * Add Date support
  * Add \x escape sequence support [issue 39](http://code.google.com/p/json-smart/issues/detail?id=39)
  * fix issue [issue 37](http://code.google.com/p/json-smart/issues/detail?id=37)

### *V 2.0-RC2* (2012-04-03)
  * Fix critical [issue 23](http://code.google.com/p/json-smart/issues/detail?id=23)
  * Improve Javadoc in JSONStyle [issue 24](http://code.google.com/p/json-smart/issues/detail?id=23)

### *V 2.0-RC1* (2012-02-18)
  * speed improvement in POJO manipulation
  * add JSONStyle.LT_COMPRESS predefined generate strct json, but ignoring / escapement.