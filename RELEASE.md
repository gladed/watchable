# Create release

* Update README.md (badges, usage), HISTORY.md (version update description), and `build.gradle` (published version number).
* Run `./gradlew check detekt dokkaGfm bintrayUpload`
* Use git to commit, tag, and push
* Move `build/gfm` to `$VERSION`, `git checkout gh-pages`, add the folder and update `index.md` to point to it. Update `latest` to link to the current version as well. Commit and push.
* Visit [BinTray](https://bintray.com/gladed/watchable/watchable). Click "Publish".