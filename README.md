# Mindlessly Hiragana
An Android app to learn Japanese Hiragana by pure mindless repetition.

[English](README.md) | [Indonesian](README.id.md)



## Index
* [Description](#description)
* [Getting Started](#getting-started)
* [Implemented Skills](#implemented-skills)
* [Implemented Concepts](#implemented-concepts)
* [About | Motivation](#about--motivation)



## Description
Usually, someone who wants to be able to read hiragana will do it by memorizing the table, by
writing it again and again on paper, etc. Those actions are something that takes a lot of cognitive
effort and something that cannot be done while watching videos.

This app is made for someone who wants to be able to read hiragana by mindlessly selecting 
buttons, a cognitively low effort action, so that they can learn while watching videos.

One tester who is 60+ years old is actually able to read any Hiragana taught in this app in under a 
week without any supervision.

This app is also made mainly to demonstrate understanding of official layered architecture and 
test-driven development.



### Screenshots
* [App Interactions on Device](readme-media/app-interactions.mp4)
* [Home Screen](readme-media/home-screen.jpg)
* [Learn Screen](readme-media/learn-screen.jpg)
* [Quiz Screen](readme-media/quiz-screen.jpg)
* [Result Screen](readme-media/result-screen-all-correct.jpg)
* [Test Screen](readme-media/test-screen-locked.jpg)
* [Test Quiz Screen](readme-media/test-quiz-screen-keyboard-1.jpg)
* [Test Result Screen](readme-media/test-result-screen-all-correct.jpg)
* [Licenses Screen](readme-media/licenses-screen.jpg)



### Screen Flow
Happy Path : Home Screen → Learn Screen → Quiz Screen → Result Screen (All Correct) → Test Screen
→ Test Quiz Screen → Test Result Screen (All Correct) → Learn Screen (Next Hiragana Category) → ...

Sad Path 1 : Home Screen → Learn Screen → Quiz Screen → Result Screen (Some Incorrect)
→ Quiz Screen → ...

Sad Path 2 : Home Screen → Test Screen → Test Quiz Screen → Test Result Screen (Some Incorrect)
→ Test Screen → ...

Home Screen → Licenses Screen



### Features
#### Home Screen
The user first sees this screen when they open the app. 

All 46 hiragana are categorized on this screen. All categories except for the first one are locked 
by default. The user will need to advance sequentially through each category. The categories are 
ordered from the most distinct to the most similar hiragana. Clicking a category will navigate the 
user to `Learn Screen`.

The user can navigate to `Test Screen` from here.

This screen provides a left drawer. The user can access dialog to reset their category progress or 
navigate to `Licenses Screen` by selecting the corresponding drawer items. 

The user is recommended to reset their category progress if they find the current `Test Screen` too 
hard.

#### Learn Screen
The user can set their `Learning Sets` here from `1` to `10` with `5` as the default. `Learning Sets` 
of `5` on `ひみかせ` category means that the user will be asked five times for each hiragana totaling 
into 20 questions.  

The user is recommended to get all questions correct with the default value at least once.

#### Quiz Screen
The user is shown a hiragana and possible romaji as selectable answers.

The user is expected to not think too hard when selecting an answer.

When the user choose incorrect answer, that button will be disabled, eventually guiding user to 
the right answer.

#### Result Screen
The user can see what hiragana they have answered and how many times they answered incorrectly.

The user can try again the `Quiz Screen` or navigate to `Test Screen` by selecting the 
corresponding buttons.

The button to navigate to `Test Screen` is only enabled when all questions from `Quiz Screen` 
are answered correctly.

#### Test Screen
The user is shown a list of hiragana categories that are being tested based on `Home Screen`'s 
unlocked categories.

The user needs to first challenge getting all questions correct on `Learn Screen` so that the 
`Test All Learned` button is enabled.

#### Test Quiz Screen
The user flow in this screen is similar to that of `Quiz Screen`, but with all hiragana's romaji as 
selectable answers.

Every hiragana question is only shown once on this screen.

#### Test Result Screen
If all questions are answered correctly on `Test Quiz Screen`, then a new hiragana category on 
`Home Screen` is unlocked, and the `Continue Learning` button is enabled.

#### Licenses Screen
This screen lists all licenses of open source libraries used by this app.



## Getting Started
### Installing
* Download the APK's build and install it on an Android device.



### Version History
* 1.0 : Minimum viable product with only basic features.



### Git Branches Details
* `master` is the only branch intended to be built.
* `readme`, `gplay-licenses-activity`, `firebase-analytics-implementation`, `refactor-applicationid`,
`refactor-robolectric`, `tdd-refactor-to-bdd`, `main-tdd`, `record-interaction` are the branches 
for features or refactors merged into main.
* Branches other than the one mentioned above exist only for historical reasons.



### Tested Devices
* Pixel 9 Pro Fold (emulator)
* Redmi Note 12 (physical device)



## Implemented Skills
* UI: Jetpack Compose | Material Design 3
* Dependency Injection: Hilt
* Testing: Robolectric | Gherkin
* Asynchronous: Kotlin Flow
* Navigation: Jetpack Navigation 2
* Source Control: Git | GitHub
* Libraries: Firebase Analytics | Google Play Services



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

I learned that doing development without any test code means that I need to manually open an 
emulator and test every feature and hope that the previous feature didn't break when I implement 
any new feature, but they did break.

Then I revert back all the changes I have made in this project to the first commit. Learning how 
important tests are, I was captivated by the promise of Test Driven Development (TDD). Then I 
reimplemented the project from scratch without consulting any code I reverted. Both implementations 
only refer to a Figma project I made beforehand. I implemented the instrumented tests with the 
Compose Testing API. After that I wondered how to reduce the amount of duplication in my
tests' code, and I found a concept called Robot Pattern (shoutout to Jake Wharton) that I very much 
like.

I experienced by hand how stress-free it is to be sure that any change you made to your project 
doesn't break any feature you have made before by following TDD. But I did also feel how slow it is 
to develop a test before a feature, but this is probably mainly because I am still learning the 
Compose Testing APIs. Nonetheless, thanks to TDD, every feature was tested.

After a lot of reading on software testing designs, I realize that the Robot Pattern is probably 
suited to a much larger project and is too overblown for my simple project. Then I was sold on the 
idea of Behavior Driven Development (BDD) and Gherkin. While I was implementing the tests, it was 
tedious to change any test because the test code is all over the place. Hence, I make sure that 
every test is contained in a single test function with its test behavior described in Gherkin 
comments. The tests are also required to have no implementation details in them, by only describing 
and testing behaviors from the user's perspective.

At this point, I actually consider this project finished, because when I ask my mother to use this 
app, she, who is 60+ years old, actually able to read any Hiragana taught in this app in under a 
week. But when I search for junior job openings, a lot of them ask for experience in Firebase and 
Google Play integrations. Thankfully, the Google team does a great job at making the integrations 
as easy as possible. I implemented the menu to look at open-source libraries used by this app with 
the Google Play OSS Licenses library and implemented simple user learning progress tracking with 
Firebase Analytics custom events.

This final implementation is the version 1.0 release build.

(I also wanted to publish to the Google Play Store, but finding private testers for my app is close 
to impossible for me right now, not to mention the price to make the Play Console Developer 
Account.)