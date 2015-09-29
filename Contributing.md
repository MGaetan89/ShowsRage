# Contributing to *ShowsRage*

You can contribute to *ShowsRage* in various ways. Any kind of contribution is welcome. The simplest one: use, share and talk about *ShowsRage* :smile:

1. [Bug Report](#bug-report)
2. [New Feature](#new-feature)
3. [Translation](#translation)

## Bug Report

It is possible that you find a bug while using *ShowsRage*. To help me fix it as fast as possible, try to provide as many information as you can when [creating an issue](https://github.com/MGaetan89/ShowsRage/issues/new).

Try to answer the following questions:
- Am I using the latest version of *ShowsRage*?
- Did someone already reported this issue?
- What is not working?
- What should have happened?
- How can I reproduce the issue?
- What is my device?
- What is my Android version?
- What is my *ShowsRage* version?

## New Feature

If you think that something is missing from *ShowsRage*, you can [open a new issue](https://github.com/MGaetan89/ShowsRage/issues/new) to explain it. As for bug report, try to give as many detail as you can.

- How should this new feature work?
- What will it bring to *ShowsRage* and all the users?

## Translation

While not everything can be translated, *ShowsRage* aim to localized as much as possible.

The following languages are currently supported:
- [English](https://github.com/MGaetan89/ShowsRage/blob/master/app/src/main/res/values/strings.xml)
- [French](https://github.com/MGaetan89/ShowsRage/blob/master/app/src/main/res/values-fr/strings.xml)

To provide a new translation, copy one of the above file into its own folder, localized, and translate it. You can add it in the list above and update the `resConfigs` field in [`app/build.gradle`](https://github.com/MGaetan89/ShowsRage/blob/master/app/build.gradle).

Once ready, send a new Pull Request so it can be included in the next version of *ShowsRage*.
