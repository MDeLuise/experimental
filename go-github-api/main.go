package main

import (
    "fmt"
    "log"
    "main/credentials"

    "github.com/google/go-github/github"
    "golang.org/x/net/context"
)

const (
    username = "MDeLuise"
)

func langsToPercentages(langs map[string]int) map[string]float32 {
    result := make(map[string]float32)
    var sum int
    for _, langByte := range langs {
        sum += langByte
    }
    for lang, langByte := range langs {
        result[lang] = calcPercentage(langByte, sum)
    }
    return result
}

func calcPercentage(value, total int) float32 {
    return float32(value*100) / float32(total)
}

func listRepositories(client *github.Client, ctx context.Context) {
    repos, _, err := client.Repositories.List(ctx, username, nil)
    if err != nil {
        log.Fatal(err.Error())
    }
    for _, repo := range repos {
        lan, _, err := client.Repositories.ListLanguages(ctx, username, *repo.Name)
        if err != nil {
            fmt.Println("Error fetching repository languages")
        }
        fmt.Println(*repo.Name, langsToPercentages(lan))
    }
}

func countTotalPR(client *github.Client, ctx context.Context) int {
    options := &github.IssueListOptions{
        State:  "all",
        Filter: "assigned",
    }
    options.Page = 0
    options.PerPage = 100

    assignedIssues := make([]*github.Issue, 0)
    requireNextPage := true
    for requireNextPage {
        options.Page++
        log.Printf("(assigned issues) Fetching page %v", options.Page)
        assignedIssuesPerPage, _, err := client.Issues.List(ctx, true, options)
        if err != nil {
            fmt.Printf("Error fething assigned issues: %v", err)
            return 0
        }
        assignedIssues = append(assignedIssues, assignedIssuesPerPage...)
        requireNextPage = len(assignedIssuesPerPage) == options.PerPage
    }

    options.Filter = "created"
    options.Page = 0
    createdIssues := make([]*github.Issue, 0)
    requireNextPage = true
    for requireNextPage {
        options.Page++
        log.Printf("(created issues) Fetching page %v", options.Page)
        createdIssuesPerPage, _, err := client.Issues.List(ctx, true, options)
        if err != nil {
            fmt.Printf("Error fething created issues: %v", err)
            return 0
        }
        log.Printf("--- (created issue in page) %v", len(createdIssuesPerPage))
        createdIssues = append(createdIssues, createdIssuesPerPage...)
        requireNextPage = len(createdIssuesPerPage) == options.PerPage
    }

    createdAndAssignedIssue := append(assignedIssues, createdIssues...)
    return len(createdAndAssignedIssue)
}

func countTotalPRInOrganizations(client *github.Client, ctx context.Context, org string) int {
    options := &github.IssueListOptions{
        State:  "all",
        Filter: "assigned",
    }
    options.Page = 0
    options.PerPage = 100

    assignedIssues := make([]*github.Issue, 0)
    requireNextPage := true
    for requireNextPage {
        options.Page++
        log.Printf("(assigned issues) Fetching page %v", options.Page)
        assignedIssuesPerPage, _, err := client.Issues.ListByOrg(ctx, org, options)
        if err != nil {
            fmt.Printf("Error fething assigned issues: %v", err)
            return 0
        }
        assignedIssues = append(assignedIssues, assignedIssuesPerPage...)
        requireNextPage = len(assignedIssuesPerPage) == options.PerPage
    }

    options.Filter = "created"
    options.Page = 0
    createdIssues := make([]*github.Issue, 0)
    requireNextPage = true
    for requireNextPage {
        options.Page++
        log.Printf("(created issues) Fetching page %v", options.Page)
        createdIssuesPerPage, _, err := client.Issues.ListByOrg(ctx, org, options)
        if err != nil {
            fmt.Printf("Error fething created issues: %v", err)
            return 0
        }
        log.Printf("--- (created issue in page) %v", len(createdIssuesPerPage))
        createdIssues = append(createdIssues, createdIssuesPerPage...)
        requireNextPage = len(createdIssuesPerPage) == options.PerPage
    }
    createdAndAssignedIssue := append(assignedIssues, createdIssues...)
    return len(createdAndAssignedIssue)
}

func countIssues(client *github.Client, ctx context.Context, orgs ...string) int {
    return countTotalPR(client, ctx) + countTotalPRInOrganizations(client, ctx, orgs[0])
}

func main() {
    ctx, client := credentials.GetClient()

    listRepositories(client, ctx)
    fmt.Print(countIssues(client, ctx, "eclipse"))
}
