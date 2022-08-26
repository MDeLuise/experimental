package credentials

import (
    "log"
    "os"

    "golang.org/x/oauth2"
    "golang.org/x/net/context"
    "github.com/google/go-github/github"
    "github.com/joho/godotenv"
)


func getToken() string {
    env := os.Getenv("GO_ENV")
    if "" == env {
        err := godotenv.Load()
        if err != nil {
            log.Fatal("Error loading .env file")
        }
    }
    return os.Getenv("GITHUB_TOKEN")
}


func GetClient() (context.Context, *github.Client) {
    token := getToken()
    ctx := context.Background()
    ts := oauth2.StaticTokenSource(
        &oauth2.Token{AccessToken: token},
    )
    tc := oauth2.NewClient(ctx, ts)
    return ctx, github.NewClient(tc)
}