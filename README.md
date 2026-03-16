# Mindlessly Hiragana

Android app to learn Japanese Hiragana by repetition

[English](README.md) | [Indonesian](README.id.md)

## Description

This app is made mainly to demonstrate understanding of Official Layered Architecture and Test Driven Development

## Getting Started

### Installing

* Download the apk's build and install it on an Android device

### Version History

* 1.0
  * Release with only basic features

### Git Branches Details
* `master` is the only branch intended to be built
* `readme`, `gplay-licenses-activity`, `firebase-analytics-implementation`, `refactor-applicationid`,
`refactor-robolectric`, `tdd-refactor-to-bdd`, `main-tdd`, `record-interaction` are the branches 
for features or refactors merged into main
* branches other than the one mentioned above exists only for historical reason

### Tested Devices

* Pixel 9 Pro Fold (emulator)
* Redmi Note 12 (device)


## Implemented Skills

### UI
* Jetpack Compose
* Material Design 3

### Dependency Injection
* Hilt

### Testing
* Robolectric
* Gherkin

### Asynchronous 
* Kotlin Flow

### Navigation
* Jetpack Navigation 2

### Source Control
* Git
* GitHub

### Libraries
* Firebase Analytics
* Google Play Services


## Implemented Concepts

* Unidirectional Data Flow
* Repository Pattern
* Reactive Programming
* Dependency Injection
* State Hoisting
* Test Coverage
* Code Readability
* Magic Value Elimination


## About | Motivation
I made this project to demonstrate my understanding of the Official Layered Architecture as 
described in the Android documentation. I first made this project as fast as possible to produce an
MVP that is small but workable with no architecture in my mind. Then I implemented the architecture 
recommended in the documentation. At this point there is still no testing in place.

I learned that doing development without any test code means that I need to manually open emulator 
and tests every feature and hope that the previous feature didn't break when i implement any new 
feature, but they did break.

Then I revert back all the changes I have made in this project to the first commit. Learning how 
important tests are, I was captivated by the promise of Test Driven Development (TDD). Then I 
reimplemented the project from scratch without consulting to any code I reverted. Both 
implementations only refer to a Figma project I made beforehand. I implemented the instrumented 
tests with Compose Testing API. After that I wonder how to reduce the amount of duplication in my
tests code, and i found concept called Robot Pattern (shoutout to Jake Wharton) that i very much 
like.

I experienced by hand how stress-free it is to be sure that any change you made to your project 
doesn't break any feature you have made before by following TDD. But I did also feel how slow it is 
to develop a test before a feature, but this is probably mainly because I am still learning the 
Compose Testing APIs. Nonetheless, thanks to TDD every feature was tested.

After a lot of reading on software testing designs, I realize that Robot Pattern is probably suited 
to a much larger project, and is too overblown for my simple project. Then I was sold to the idea 
of Behavior Driven Development (BDD) and Gherkin. While I was implementing the tests, it was 
tedious to change any test because the test code is all over the place. Hence, I make sure that 
every test is contained in a single test function with its test behavior described in Gherkin 
comment. The tests are also required to have no implementation details in it, by only describing 
and testing behaviors from the user's perspective.

At this point, I actually consider this project finished, because when I ask my mother to use this 
app, she who is 60+ years old actually able to read any Hiragana taught in this app in under a 
week. But when I search for junior job openings, a lot of them asks for experience in Firebase and 
Google Play integrations. Thankfully, the Google team does a great job at making the integrations 
as easy as possible. I implemented the menu to look at open source libraries used by this app with 
Google Play OSS Licenses library, and implemented simple user learning progress tracking with 
Firebase Analytics custom events.

This final implementation is the version 1.0 release build.

(I also wanted to publish to Google Play Store, but finding private tester for my app is close to 
impossible for me right now, not to mention the price to make the Play Console Developer Account)

## Things I Learned
- React JS - I knew React before but was still rusty in it a little bit so this project gave me a lot of confidence in using it and hopefully I'll be able to create more interesting projects with it and increase my proficiency.
- Framer-Motion - I was familiar with this animation library via Youtube and was just astounded how easy it was to use and integrate inside the code to get some smooth animations!
- Sanity - Never heard of Sanity before this project came along. A super simple Content Management System so that people who don't know how to write code can easily change things for their website without ever breaking it. The ease of use with which you can customize the content inside it is insane.
- UI/UX Design Flows - Learning UI/UX design as a developer solidifies your understanding of good design and workflows.

## Problems I Encountered
Of Course there are always error that may pop up when creating a project. These were some of them:
- Render problems for some React components due to some names that I misspelled, a silly mistake which happens a lot in programming.
- Sanity failed to reload when I added something new to the website, after spending some time on the internet, I was able to find a solution.
- Some styling bugs in various devices which were solved by playing around with them for some time.

## My Takeaways
This is probably my first "Proper" personal project that I made which is live on the internet. Along with the techy "things" that I learned, I would like to share some general tips that may help others while creating big projects like this one.
- TAKE BREAKS! It's important to feel fresh when you're working on something and it's not a race so its okay if a bug is taking you a whole day to solve or you just don't have the energy to do anything right now.
- Don't let your ego get in the way of being efficient. Use whichever tools you like whether other people use them or not. You need to be comfortable with what you work with.
- Ask for help whenever you can, from wherever you can. The ego part comes again here. You don't know everything and neither does other programmers. We are all in an awesome community and we are here to help each other grow.
