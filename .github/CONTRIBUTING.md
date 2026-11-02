# How to Contribute

You are welcome to create pull requests or join in our mailing list for bugfix, doc, example, suggestion or anything else.

## Pull requests

Every pull request targets `develop`, the default branch.

### Stacked pull requests

A pull request may build on the branch of another one that is still open, and target that branch so
that its diff shows only its own commits. Such a stack is merged into `develop` from the bottom up,
never into the branch it is based on:

1. Merge the bottom pull request into `develop`.
2. Its branch is deleted on merge, and GitHub retargets the next pull request to `develop`. Rebase
   that branch onto `develop` if it no longer applies cleanly, and wait for its CI build.
3. Merge it into `develop`, and go on with the next one.

A pull request merged into the branch of another one is not on `develop` until that one is merged:
the CI build (`.github/workflows/gradle.yml`) runs only for pull requests into `develop` and
`master`, so nothing checked the merge, and the `Closes #N` of its description does not close the
issue, as GitHub closes issues only on a merge into the default branch (#59 had to be closed by
hand after #64 was merged into the branch of #62).
