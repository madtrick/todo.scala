# About

A project to learn Scala

## Setup

The project uses [mill](https://mill-build.com/mill/Intro_to_Mill.html) as its build tool. I configured `metals` use `mill` as its build server (instead of `bloop`).

## Development

I'm using [Metals](https://scalameta.org/metals/) together with neovim, via the [nvim-metals](https://github.com/scalameta/nvim-metals) plugin. So far it seems to be working find besides some [ hiccups ](https://github.com/scalameta/nvim-metals/issues/623).

## Modules

The project is split into three modules:

- [Cli](./todo/Cli/). Provides a command line interface to manage todos
- [Webserver](./todo/WebServer/). Provides a REST-like API to manage todos
- [Domain](./todo/Domain/). Provides domain entities and business logic to manage todos. It's used both by the `Cli` and the `Webserver`

## Useful commands

- `../mill <Module>.run`. Runs the app inside the given module
- `../mill <Module>.test`. Runts the tests inside the given module

## TODO commands

- `../mill Cli.run add <action>`
- `../mill Cli.run complete -i <index>`
- `../mill Cli.run list`
- `../mill Cli.run delete -i <index>`

## Notes

- `mill` expects the project folder structure to follow that of modules in the build file. On this project the build file declares a `Todo` module and therefore we have a `Todo` folder. Inside it we have a nested `test` module (both in the build file as well as in the folder layout).
