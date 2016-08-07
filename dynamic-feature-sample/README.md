# DynamicFeatureでフィルターを適用するサンプル

## 概要

`DynamicFeature`を使うと`configure`メソッドで独自の条件判定をして、
リソースメソッドにフィルター(インターセプター)を適用するかどうかを決められます。

その際、引数の`ResourceInfo`からリソースメソッドの情報を取得できます。

このサンプルでは、`PrefixFeature`が`DynamicFeature`を実装しており、
`@Prefix`で注釈されているリソースメソッドに対してフィルターを適用しています。

## サンプルの動かし方

次のコマンドでPayara Microが起動してサンプルアプリケーションがデプロイされます。

```sh
gradlew run
```

起動したら下記のように幾つかのURLへアクセスしてみてください。

```sh
curl localhost:8080/sample/api/foo
curl localhost:8080/sample/api/foo/bar
curl localhost:8080/sample/api/foo/baz
```

