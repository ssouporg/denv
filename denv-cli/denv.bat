@echo off

set ERROR_CODE=0

if not "%JAVA_HOME%" == "" goto OkJHome

echo.
echo ERROR: JAVA_HOME not found in your environment.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto error

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory.
echo JAVA_HOME = "%JAVA_HOME%"
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto error

:init

@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of agruments (up to the command line limit, anyway).
set DENV_CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto endInit
set DENV_CMD_LINE_ARGS=%DENV_CMD_LINE_ARGS% %1
shift
goto Win9xApp

@REM Reaching here means variables are defined and arguments have been captured
:endInit
SET DENV_JAVA_EXE="%JAVA_HOME%\bin\java.exe"

@REM Start DENV
:runDenv

@REM Development classpath
set DENV_CLASSPATH="C:\workspaces\denv\denv-cli\target\classes;C:\workspaces\denv\denv-client\target\classes;C:\workspaces\denv\denv-core-containerization\target\classes;C:\workspaces\denv\denv-core\target\classes;C:\Users\ALB\.m2\repository\org\springframework\boot\spring-boot-starter\1.1.8.RELEASE\spring-boot-starter-1.1.8.RELEASE.jar;C:\Users\ALB\.m2\repository\org\springframework\boot\spring-boot\1.1.8.RELEASE\spring-boot-1.1.8.RELEASE.jar;C:\Users\ALB\.m2\repository\org\springframework\spring-core\4.0.7.RELEASE\spring-core-4.0.7.RELEASE.jar;C:\Users\ALB\.m2\repository\org\springframework\spring-context\4.0.7.RELEASE\spring-context-4.0.7.RELEASE.jar;C:\Users\ALB\.m2\repository\org\springframework\spring-aop\4.0.7.RELEASE\spring-aop-4.0.7.RELEASE.jar;C:\Users\ALB\.m2\repository\aopalliance\aopalliance\1.0\aopalliance-1.0.jar;C:\Users\ALB\.m2\repository\org\springframework\spring-beans\4.0.7.RELEASE\spring-beans-4.0.7.RELEASE.jar;C:\Users\ALB\.m2\repository\org\springframework\spring-expression\4.0.7.RELEASE\spring-expression-4.0.7.RELEASE.jar;C:\Users\ALB\.m2\repository\org\springframework\boot\spring-boot-autoconfigure\1.1.8.RELEASE\spring-boot-autoconfigure-1.1.8.RELEASE.jar;C:\Users\ALB\.m2\repository\org\springframework\boot\spring-boot-starter-logging\1.1.8.RELEASE\spring-boot-starter-logging-1.1.8.RELEASE.jar;C:\Users\ALB\.m2\repository\org\slf4j\jcl-over-slf4j\1.7.7\jcl-over-slf4j-1.7.7.jar;C:\Users\ALB\.m2\repository\org\slf4j\slf4j-api\1.7.7\slf4j-api-1.7.7.jar;C:\Users\ALB\.m2\repository\org\slf4j\jul-to-slf4j\1.7.7\jul-to-slf4j-1.7.7.jar;C:\Users\ALB\.m2\repository\org\slf4j\log4j-over-slf4j\1.7.7\log4j-over-slf4j-1.7.7.jar;C:\Users\ALB\.m2\repository\ch\qos\logback\logback-classic\1.1.2\logback-classic-1.1.2.jar;C:\Users\ALB\.m2\repository\ch\qos\logback\logback-core\1.1.2\logback-core-1.1.2.jar;C:\Users\ALB\.m2\repository\org\yaml\snakeyaml\1.13\snakeyaml-1.13.jar;C:\Users\ALB\.m2\repository\org\springframework\data\spring-data-commons\1.8.4.RELEASE\spring-data-commons-1.8.4.RELEASE.jar;C:\Users\ALB\.m2\repository\org\springframework\integration\spring-integration-http\4.0.4.RELEASE\spring-integration-http-4.0.4.RELEASE.jar;C:\Users\ALB\.m2\repository\org\springframework\spring-webmvc\4.0.7.RELEASE\spring-webmvc-4.0.7.RELEASE.jar;C:\Users\ALB\.m2\repository\org\springframework\spring-web\4.0.7.RELEASE\spring-web-4.0.7.RELEASE.jar;C:\Users\ALB\.m2\repository\net\java\dev\rome\rome-fetcher\1.0.0\rome-fetcher-1.0.0.jar;C:\Users\ALB\.m2\repository\jdom\jdom\1.0\jdom-1.0.jar;C:\Users\ALB\.m2\repository\xerces\xercesImpl\2.4.0\xercesImpl-2.4.0.jar;C:\Users\ALB\.m2\repository\net\java\dev\rome\rome\1.0.0\rome-1.0.0.jar;C:\Users\ALB\.m2\repository\commons-httpclient\commons-httpclient\3.0.1\commons-httpclient-3.0.1.jar;C:\Users\ALB\.m2\repository\commons-codec\commons-codec\1.2\commons-codec-1.2.jar;C:\Users\ALB\.m2\repository\org\springframework\integration\spring-integration-core\4.0.4.RELEASE\spring-integration-core-4.0.4.RELEASE.jar;C:\Users\ALB\.m2\repository\org\springframework\spring-tx\4.0.7.RELEASE\spring-tx-4.0.7.RELEASE.jar;C:\Users\ALB\.m2\repository\org\springframework\retry\spring-retry\1.1.1.RELEASE\spring-retry-1.1.1.RELEASE.jar;C:\Users\ALB\.m2\repository\org\springframework\spring-messaging\4.0.7.RELEASE\spring-messaging-4.0.7.RELEASE.jar;C:\Users\ALB\.m2\repository\com\fasterxml\jackson\core\jackson-core\2.3.4\jackson-core-2.3.4.jar;C:\Users\ALB\.m2\repository\com\fasterxml\jackson\core\jackson-databind\2.3.4\jackson-databind-2.3.4.jar;C:\Users\ALB\.m2\repository\com\fasterxml\jackson\core\jackson-annotations\2.3.4\jackson-annotations-2.3.4.jar;C:\Users\ALB\.m2\repository\org\springframework\hateoas\spring-hateoas\0.16.0.RELEASE\spring-hateoas-0.16.0.RELEASE.jar;C:\Users\ALB\.m2\repository\org\objenesis\objenesis\2.1\objenesis-2.1.jar;C:\Users\ALB\.m2\repository\org\springframework\boot\spring-boot-starter-test\1.1.8.RELEASE\spring-boot-starter-test-1.1.8.RELEASE.jar;C:\Users\ALB\.m2\repository\junit\junit\4.11\junit-4.11.jar;C:\Users\ALB\.m2\repository\org\hamcrest\hamcrest-core\1.3\hamcrest-core-1.3.jar;C:\Users\ALB\.m2\repository\org\mockito\mockito-core\1.9.5\mockito-core-1.9.5.jar;C:\Users\ALB\.m2\repository\org\hamcrest\hamcrest-library\1.3\hamcrest-library-1.3.jar;C:\Users\ALB\.m2\repository\org\springframework\spring-test\4.0.7.RELEASE\spring-test-4.0.7.RELEASE.jar;C:\Users\ALB\.m2\repository\com\beust\jcommander\1.30\jcommander-1.30.jar;C:\Users\ALB\.m2\repository\org\springframework\boot\spring-boot-loader-tools\1.1.8.RELEASE\spring-boot-loader-tools-1.1.8.RELEASE.jar;C:\Program Files (x86)\JetBrains\IntelliJ IDEA Community Edition 13.1.5\lib\idea_rt.jar"

@REM Prod classpath
@REM set DENV_CLASSPATH="..\lib\denv-cli.jar;..\lib\denv-client.jar;..\lib\denv-core-containerization.jar;..\lib\denv-core.jar;..\lib\spring-boot-starter-1.1.8.RELEASE.jar;..\lib\spring-boot-1.1.8.RELEASE.jar;..\lib\spring-core-4.0.7.RELEASE.jar;..\lib\spring-context-4.0.7.RELEASE.jar;C:\Users\ALB\.m2\repository\org\springframework\spring-aop\4.0.7.RELEASE\spring-aop-4.0.7.RELEASE.jar;..\lib\aopalliance-1.0.jar;..\lib\spring-beans-4.0.7.RELEASE.jar;..\lib\spring-expression-4.0.7.RELEASE.jar;C:\Users\ALB\.m2\repository\org\springframework\boot\spring-boot-autoconfigure\1.1.8.RELEASE\spring-boot-autoconfigure-1.1.8.RELEASE.jar;C:\Users\ALB\.m2\repository\org\springframework\boot\spring-boot-starter-logging\1.1.8.RELEASE\spring-boot-starter-logging-1.1.8.RELEASE.jar;C:\Users\ALB\.m2\repository\org\slf4j\jcl-over-slf4j\1.7.7\jcl-over-slf4j-1.7.7.jar;C:\Users\ALB\.m2\repository\org\slf4j\slf4j-api\1.7.7\slf4j-api-1.7.7.jar;C:\Users\ALB\.m2\repository\org\slf4j\jul-to-slf4j\1.7.7\jul-to-slf4j-1.7.7.jar;C:\Users\ALB\.m2\repository\org\slf4j\log4j-over-slf4j\1.7.7\log4j-over-slf4j-1.7.7.jar;C:\Users\ALB\.m2\repository\ch\qos\logback\logback-classic\1.1.2\logback-classic-1.1.2.jar;C:\Users\ALB\.m2\repository\ch\qos\logback\logback-core\1.1.2\logback-core-1.1.2.jar;C:\Users\ALB\.m2\repository\org\yaml\snakeyaml\1.13\snakeyaml-1.13.jar;C:\Users\ALB\.m2\repository\org\springframework\data\spring-data-commons\1.8.4.RELEASE\spring-data-commons-1.8.4.RELEASE.jar;C:\Users\ALB\.m2\repository\org\springframework\integration\spring-integration-http\4.0.4.RELEASE\spring-integration-http-4.0.4.RELEASE.jar;C:\Users\ALB\.m2\repository\org\springframework\spring-webmvc\4.0.7.RELEASE\spring-webmvc-4.0.7.RELEASE.jar;C:\Users\ALB\.m2\repository\org\springframework\spring-web\4.0.7.RELEASE\spring-web-4.0.7.RELEASE.jar;C:\Users\ALB\.m2\repository\net\java\dev\rome\rome-fetcher\1.0.0\rome-fetcher-1.0.0.jar;C:\Users\ALB\.m2\repository\jdom\jdom\1.0\jdom-1.0.jar;C:\Users\ALB\.m2\repository\xerces\xercesImpl\2.4.0\xercesImpl-2.4.0.jar;C:\Users\ALB\.m2\repository\net\java\dev\rome\rome\1.0.0\rome-1.0.0.jar;C:\Users\ALB\.m2\repository\commons-httpclient\commons-httpclient\3.0.1\commons-httpclient-3.0.1.jar;C:\Users\ALB\.m2\repository\commons-codec\commons-codec\1.2\commons-codec-1.2.jar;C:\Users\ALB\.m2\repository\org\springframework\integration\spring-integration-core\4.0.4.RELEASE\spring-integration-core-4.0.4.RELEASE.jar;C:\Users\ALB\.m2\repository\org\springframework\spring-tx\4.0.7.RELEASE\spring-tx-4.0.7.RELEASE.jar;C:\Users\ALB\.m2\repository\org\springframework\retry\spring-retry\1.1.1.RELEASE\spring-retry-1.1.1.RELEASE.jar;C:\Users\ALB\.m2\repository\org\springframework\spring-messaging\4.0.7.RELEASE\spring-messaging-4.0.7.RELEASE.jar;C:\Users\ALB\.m2\repository\com\fasterxml\jackson\core\jackson-core\2.3.4\jackson-core-2.3.4.jar;C:\Users\ALB\.m2\repository\com\fasterxml\jackson\core\jackson-databind\2.3.4\jackson-databind-2.3.4.jar;C:\Users\ALB\.m2\repository\com\fasterxml\jackson\core\jackson-annotations\2.3.4\jackson-annotations-2.3.4.jar;C:\Users\ALB\.m2\repository\org\springframework\hateoas\spring-hateoas\0.16.0.RELEASE\spring-hateoas-0.16.0.RELEASE.jar;C:\Users\ALB\.m2\repository\org\objenesis\objenesis\2.1\objenesis-2.1.jar;C:\Users\ALB\.m2\repository\org\springframework\boot\spring-boot-starter-test\1.1.8.RELEASE\spring-boot-starter-test-1.1.8.RELEASE.jar;C:\Users\ALB\.m2\repository\junit\junit\4.11\junit-4.11.jar;C:\Users\ALB\.m2\repository\org\hamcrest\hamcrest-core\1.3\hamcrest-core-1.3.jar;C:\Users\ALB\.m2\repository\org\mockito\mockito-core\1.9.5\mockito-core-1.9.5.jar;C:\Users\ALB\.m2\repository\org\hamcrest\hamcrest-library\1.3\hamcrest-library-1.3.jar;C:\Users\ALB\.m2\repository\org\springframework\spring-test\4.0.7.RELEASE\spring-test-4.0.7.RELEASE.jar;C:\Users\ALB\.m2\repository\com\beust\jcommander\1.30\jcommander-1.30.jar;C:\Users\ALB\.m2\repository\org\springframework\boot\spring-boot-loader-tools\1.1.8.RELEASE\spring-boot-loader-tools-1.1.8.RELEASE.jar;C:\Program Files (x86)\JetBrains\IntelliJ IDEA Community Edition 13.1.5\lib\idea_rt.jar"

%DENV_JAVA_EXE% -classpath %DENV_CLASSPATH% org.ssoup.denv.cli.Main %DENV_CMD_LINE_ARGS%
if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
@REM before we started - at least we don't leave any baggage around
set DENV_JAVA_EXE=

cmd /C exit /B %ERROR_CODE%
