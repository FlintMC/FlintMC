 #Labyfy
 ###### Open Source modding framework provided by LabyMedia GmbH

to build labyfy, you need to perform the `before` steps from the .gitlab-ci.yml. (create folder and stuff)
You also need the following values in your $HOME/.gradle/gradle.properties

```
artifactory_contextUrl=https://maven.laby.tech/artifactory/
### auth if you want to publish
artifactory_user=gitlab
artifactory_password=<password>
org.gradle.caching=true
gradle.cache.push=false
```

after your first checkout, execute 
`versioned/labyfy-1.15.1/tasks/other/download-libraries`
in your gradle task window.
If finished you have to reimport gradle and 
now you're able to start the client via run-configuration