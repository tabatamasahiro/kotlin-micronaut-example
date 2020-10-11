## 課題

KotlinとMicronautを利用し、書籍管理システムの構築をお願いします。
- 書籍には著者の属性があり、書籍と著者の情報をRDBに登録・変更・削除・検索できる
- 著者が決まっているもののタイトルが未定の場合がある
- 出版社はまだ出版されていない書籍に対して、タイトルを何度でも変更できる
- 出版社はまだ出版されていない書籍に対して、出版日を変更できる。ただし出版日を過去の日付には変更できない
- 著者に紐づく本を取得できる
結果はGitHubにアップし、プロジェクトのURLを送付頂けますでしょうか。
出来る限りテストも作成お願いします。
コミットは一つにまとめず適切な単位で作成ください。

## 機能説明

#### 釈明

- フォームからサーバーサイドへ日本語を送信するにあたり文字化けが発生し解決できなかった
- そのため**著者名**、**書籍タイトル**は**英数字、半角スペース**のみを許容しております

#### micronautスタック
- views: Thymeleaf
- servlet: servlet/tomcat
- databae: data-jdbc/h2
- logging: logback

#### URL

- menu ==> http://localhost:8080/book

- 登録 ==> http://localhost:8080/book/create

- 検索 ==> http://localhost:8080/book/search

- 更新 ==> http://localhost:8080/book/update

- 削除 ==> http://localhost:8080/book/delete


