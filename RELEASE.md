# Create release

* Update README.md (badges, usage), HISTORY.md (version update description), and `build.gradle` (published version number).
* Run `./gradlew check detekt bintrayUpload`
* Use git to commit, tag, and push
* Visit [BinTray](https://bintray.com/gladed/watchable/watchable). Click "Publish".
