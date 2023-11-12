# About

A project to learn Scala

## Setup

The project uses [mill](https://mill-build.com/mill/Intro_to_Mill.html) as its build tool. I configured `metals` use `mill` as its build server (instead of `bloop`).

## Development

I'm using [Metals](https://scalameta.org/metals/) together with neovim, via the [nvim-metals](https://github.com/scalameta/nvim-metals) plugin. So far it seems to be working find besides some [ hiccups ](https://github.com/scalameta/nvim-metals/issues/623).

## Useful commands

- `../mill Todo.run`
- `../mill Todo.tes`

## TODO commands

- `../mill Todo.run add <action>`
- `../mill Todo.run complete -i <index>`
- `../mill Todo.run list`
- `../mill Todo.run delete -i <index>`

## Notes

- `mill` expects the project folder structure to follow that of modules in the build file. On this project the build file declares a `Todo` module and therefore we have a `Todo` folder. Inside it we have a nested `test` module (both in the build file as well as in the folder layout).
