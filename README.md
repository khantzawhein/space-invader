# Space Invaders
## Project II of SE 233 Advanced Programming
![Space Invader Logo](./package/macos/spaceinvader-background.png)
### Introduction
This is a project of SE 233, which is a game called Space Invaders. It is a JavaFX GUI application, and it is written in Java.


### Teammates
- Khant Zaw Hein (652115511)
- Naw Joyful Awgyi (652115510)
- Win Naing Kyaw (652115517)

### How to compile and run
To compile and run the project, you need to have JDK 20 or above installed. Then, you can use the following command to compile and run the project

1. Run the `Launcher.java` from the `src/main/java/com/se233/SpaceInvaders` directory using IntelliJ.
2. Or, you can use the following command to compile and run the project
```shell
mvn package
jpackage --name SpaceInvader --input . --main-jar ./target/spaceinvader-1.0-SNAPSHOT.jar --resource-dir ./package/macos --type pkg
```

### Credits
- [Space Invaders](https://en.wikipedia.org/wiki/Space_Invaders)
- [Space Invaders - Player One - Music](https://www.youtube.com/watch?v=F2lGJMrUUHw&pp=ygUOc3BhY2UgaW52YWRlcnM%3D)
- [Game Sound Effects](https://www.classicgaming.cc/classics/space-invaders/sounds)
- [Space Invaders Fonts](https://www.classicgaming.cc/classics/space-invaders/icons-and-fonts)
- [Mario Sound Effects - Game Over](https://www.youtube.com/watch?v=BVQ_JHmvhCM&ab_channel=SuperMarioBroz)