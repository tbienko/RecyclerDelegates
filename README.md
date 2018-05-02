# RecyclerDelegates
Simple library created to make work with RecyclerView pleasant.
Use composite pattern to easily handle different types of items without writing boilerplate code.
Built-in DiffUtil support.

## Changelog
See [releases](https://github.com/tbienko/RecyclerDelegates/releases)

## How it works
It works as a abstraction layer for RecyclerView Adapter:

```
  +----------------------------------------+
  | RecyclerView                           |
  +----------------------------------------+
                      |
                      |
  +-------------------v--------------------+
  | DelegatingRecyclerAdapter              |
  |                                        |
  | RecyclerView.Adapter implementation    |
  +----------------------------------------+
                      |
                      |
  +-------------------v--------------------+
  | SimpleDelegateManager                  |
  | or other RecyclerDelegateManager impl. |
  |                                        |
  | Transforms adapter calls to            |
  | delegate calls                         |
  |                                        |
  +----------------------------------------+
            |                     |
            |                     |
  +---------v--------+  +---------v--------+
  |                  |  |                  |
  | RecyclerDelegate |  | RecyclerDelegate |
  | impl.            |  | impl.            |
  |                  |  |                  |
  +------------------+  +------------------+
```

Typically it's not needed to implement nothing other than `RecyclerDelegate`s. In some simple cases
you may use `StaticLayoutDelegate`, `TextBindingDelegate` or some other delegates

## Example
Check `app` directory