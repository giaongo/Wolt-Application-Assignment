# Wolt-Application-Assignment
This personal Wolt Android assignment is created as part of the Wolt summer engineering intern application procedure.
The app's purpose is to fetch the user's current coordinates upon which to obtain venue data from the network. This app is for users to browse venue information and 
add or remove venue from favorite list. 

## Tables of contents
* [App Features](#app-features)
* [In-app technology](#in-app-technology)
* [App Demo Video](#app-demo-video)
* [App UI](#app-ui)

## App Features
1. Browse venue information based on user's current location
2. Add or remove venue from favorite

## In-app technology
This Android app is built by Kotlin, targeting min SDK 24 and following MVVM software architectural pattern. 
This App uses Glide library to fetch image from network,  uses Fused Location Provider(Google Play services location APIs) to get current location, 
uses Retrofit & Scalar to fetch network data and json string conversion. Besides, the practise of manual JSON string deserialization is also implemented.  

Summary of components used in App: 
* FrontEnd: Activity, Fragments, RecyclerView, and CardView.
* BackEnd:
1. Database: ROOM database with Coroutine, LiveData and follow one-to-one relationship pattern
2. Network: Retrofit, Scalar to fetch JSON data from network and manual json deserialization.


## App Demo Video: 
<div align="center">

[![Wolt Assignment Demo Video](http://img.youtube.com/vi/KOdTBVQa5gc/0.jpg)](https://youtu.be/KOdTBVQa5gc "Wolt Assignment Demo Video")

</div>

## App UI:
![wolt_ui](https://user-images.githubusercontent.com/83873333/213933176-2f192834-b966-4df4-b175-3554ebfc76cc.png)
