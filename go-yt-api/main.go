package main

import (
    "fmt"
    "log"
    "main/credentials"

    "google.golang.org/api/youtube/v3"
)

const (
    captionScope = "https://www.googleapis.com/auth/youtube.force-ssl"
    captionLang  = "eng"
)

func channelsListByUsername(service *youtube.Service, part []string, forUsername string) {
    call := service.Channels.List(part)
    call = call.ForUsername(forUsername)
    response, error := call.Do()
    if error != nil {
        log.Fatal(error.Error())
    }
    fmt.Println(fmt.Sprintf("This channel's ID is %s. Its title is '%s', "+
        "and it has %d views.",
        response.Items[0].Id,
        response.Items[0].Snippet.Title,
        response.Items[0].Statistics.ViewCount))
}

func getCaptionID(service *youtube.Service, videoId string) (string, bool) {
    call := service.Captions.List([]string{"snippet"}, videoId)
    response, error := call.Do()
    if error != nil {
        log.Fatal(error.Error())
    }
    for _, item := range response.Items {
        if captionLang == item.Snippet.Language {
            return item.Id, true
        }
    }

    return "", false
}

func downloadCaption(service *youtube.Service, captionID string) {
    call := service.Captions.Download(captionID)
    response, error := call.Download()
    if error != nil {
        log.Fatal(error.Error())
    }
    fmt.Println(response)
}

func main() {
    service, err := credentials.Authenticate([]string{captionScope})
    if err != nil {
        log.Fatalf(err.Error())
    }

    channelsListByUsername(service, []string{"snippet,contentDetails,statistics"}, "GoogleDevelopers")

    if captionID, ok := getCaptionID(service, "M7FIvfx5J10"); ok {
        fmt.Println("use caption with id ", captionID)
        downloadCaption(service, captionID)
    }
}
