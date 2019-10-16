# 2.6 Parsing Expressions

- Operator precedence(操作符优先级), 例如 `5 * 5 + 10`

## Monkey Expressions

Monkey语言的表达式类型

- prefix operators: `-5`, `!true`, `!false`
- infix operators(or binary operators): `5 + 5`, `5 - 5`
- comparison operators: `foo == bar`, `foo != bar`
- group expressions and influence the order of evaluation: `5 * ( 5 + 5)`
- call expressions: `add(2, 3)`, `add(add(2,3), add(5,10))`
- identifiers are expressions too: `foo * bar / foobar`, `add(foo, bar)`
- function literals are expressions too: `let add = fn(x, y) { return x + y}`
, `fn(x, y) { return x + y }(5, 5)`, `(fn(x) { return x }(5) + 10 ) * 10`
- if expressions: `let result = if (10 > 5) { true } else { false }`


## Top Down Operator Precedence ( or: Paratt Parsing )

## How Pratt Parsing Works

