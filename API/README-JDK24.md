# JDK 24 Configuration for Quarkus

This document outlines the configuration needed to run this Quarkus application with JDK 24.

## Current Status

- ✅ Quarkus updated to 3.26.2 (latest)
- ✅ Java updated to 21 (requirement met)
- ⏳ JDK 24 support documented (requires JDK 24 installation)

## JDK 24 Requirements

Based on Quarkus community findings (see issue #49708), JDK 24 requires additional JVM arguments to work properly with Quarkus applications.

### Required JVM Arguments

Add the following JVM argument to your application startup:

```
--add-opens java.base/java.lang=ALL-UNNAMED
```

### Maven Configuration for JDK 24

To enable JDK 24 support, update `pom.xml`:

```xml
<properties>
    <maven.compiler.release>24</maven.compiler.release>
    <!-- ... other properties ... -->
</properties>

<build>
    <plugins>
        <plugin>
            <groupId>io.quarkus.platform</groupId>
            <artifactId>quarkus-maven-plugin</artifactId>
            <version>${quarkus.platform.version}</version>
            <configuration>
                <jvmArgs>
                    <jvmArg>--add-opens</jvmArg>
                    <jvmArg>java.base/java.lang=ALL-UNNAMED</jvmArg>
                </jvmArgs>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### Development Mode

For development mode (`mvn quarkus:dev`), add the JVM argument:

```bash
mvn quarkus:dev -Dquarkus.args="--add-opens java.base/java.lang=ALL-UNNAMED"
```

### Production Deployment

When running the built JAR in production:

```bash
java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/quarkus-app/quarkus-run.jar
```

### Native Image (GraalVM)

For native image compilation with JDK 24, additional configuration may be required. Monitor Quarkus releases for native image support with JDK 24.

## Testing JDK 24 Compatibility

Once JDK 24 is available in your environment:

1. Update `maven.compiler.release` to `24` in `pom.xml`
2. Add the required JVM arguments as shown above
3. Test the application thoroughly
4. Monitor for any deprecation warnings or compatibility issues

## References

- [Quarkus Issue #49708](https://github.com/quarkusio/quarkus/issues/49708) - JDK 24 compatibility issues
- [Quarkus Migration Guide](https://quarkus.io/guides/migration-guide)
- [Quarkus with Java 24 on Fly.io Guide](https://yeahwellyouknowthatsjustlikeuhyouropinionman.com/2025/03/31/quarkus-with-java-24-on-fly/) (original reference)

## Current Build Environment

This project currently builds and runs successfully with:
- Quarkus 3.26.2
- Java 21
- Maven 3.9.11

JDK 24 support is documented here for future implementation when JDK 24 becomes available in the build environment.