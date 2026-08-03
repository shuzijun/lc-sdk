# Changelog

## 0.0.4

### Added

- Add a typed `LcApi` facade while preserving the published 0.0.3 command API.
- Add injectable endpoint, transport, request context and cancellation contracts.
- Add typed EN/CN user, question, code execution, note, submission, session, favorite and find APIs.
- Add Java 8-compatible typed result models and offline transport contract tests.

### Fixed

- Preserve opaque submission IDs and both actual and expected interpret IDs.
- Map run result arrays as typed lists instead of nested JSON strings.
- Map question metadata, translated fields, topics, hints and similar questions.
- Request the CN premium field consumed by the user mapper.
