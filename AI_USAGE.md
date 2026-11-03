# How silly-io is made

silly-io was written by its maintainer, Asterios Raptis, over many years and without AI tools: its
code goes back to 2015, and up to release 3.6 (2024) no AI tool had a part in it.

Since 2026 AI agents are used as a tool to maintain and modernize it. Release 5.0 was implemented
mostly by them, under the maintainer's review: 22 of the 24 commits between `RELEASE-4.0` and
`RELEASE-5.0` name an agent as co-author, and 14 of the 15 pull requests say they were generated
with it. For 5.0 the agents

- introduced mutation testing with PIT and wrote the tests it showed to be missing,
- brought the tests to every line and every branch,
- replaced the code that had been taken from Apache-2.0 projects with code of this project (#60),
- implemented the four breaking changes and wrote their migration notes,
- added the Makefile, the mutation score badge and the release gate,
- and prepared the release up to its upload to the Central Portal.

The maintainer set what 5.0 should change and decided its open questions, among them that a
missing file is treated as the JDK treats it (#56), that `PrefixFileFilter` keeps its case unless
asked otherwise (#58), and that the release is a major one. He reviewed and merged every pull
request, gave the go for the release tag, and published the release on Maven Central himself; the
artifacts are signed with his key.

The agents are tools, not authors. In this repository their commits carry a `Co-Authored-By` line
and their pull requests a "Generated with Claude Code" line. How the maintainer uses AI agents
across his projects: [How my projects are made](https://github.com/astrapi69/astrapi69/blob/main/AI_USAGE.md).
