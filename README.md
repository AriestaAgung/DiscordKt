![KUtils Server](https://discordapp.com/api/guilds/453208597082406912/widget.png?style=banner2)

The documentation for this project is currently WIP. 
The best source for learning the framework is by joining the Discord, or checking existing bots.

#### Maven
```xml
<repository>
    <id>Sonatype Snapshots</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
</repository>

<dependency>
    <groupId>me.jakejmattson</groupId>
    <artifactId>KUtils</artifactId>
    <version>0.18.1-SNAPSHOT</version>
</dependency>
```

#### Gradle
```groovy
maven {
    url 'https://oss.sonatype.org/content/repositories/snapshots/'
}

dependencies {
    implementation 'me.jakejmattson:KUtils:0.18.1-SNAPSHOT'
}
```
```kotlin
maven {
    url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    implementation("me.jakejmattson:KUtils:0.18.1-SNAPSHOT")
}
```