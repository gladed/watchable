# Create release

* Update README.md (badges, usage), HISTORY.md (version update description), and `build.gradle` (published version number).
* Run `./gradlew check detekt dokkaGfm bintrayUpload`
* Use git to commit, tag, and push
* Copy `build/gfm` to `$VERSION`, switch to gh-pages and add the folder and update `index.md` to point to it. Commit and push.
* Visit [BinTray](https://bintray.com/gladed/watchable/watchable). Click "Publish".
