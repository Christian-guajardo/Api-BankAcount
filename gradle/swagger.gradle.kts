buildscript {
    repositories { mavenCentral() }
    dependencies { classpath("io.swagger.codegen.v3:swagger-codegen-generators:1.0.31") }
    project.the<SourceSetContainer>()["main"].java.srcDir("$buildDir/swagger/src/gen/java")
    tasks.named("compileJava") { dependsOn(":generateSwagger") }
}

apply(plugin = "java-library")

dependencies {
    "implementation"("io.swagger.core.v3:swagger-annotations-jakarta:2.2.16")
    "implementation"("jakarta.annotation:jakarta.annotation-api:2.1.1")
    "implementation"("javax.validation:validation-api:2.0.1.Final")
    "implementation"("javax.annotation:javax.annotation-api:1.3.2")
}

task("generateSwagger") {
    val a="src/main/resources/swagger"
    val b="$buildDir/swagger"
    inputs.dir(a); outputs.dir(b)
    doLast {
        fileTree(File(a)){include("**/*.json","**/*.yaml","**/*.yml")}.forEach{f:File->
            val c="dto."+f.nameWithoutExtension.replace("-","")
            val d=io.swagger.codegen.v3.config.CodegenConfigurator()
            d.inputSpec=f.absolutePath
            d.outputDir=b
            d.lang="jaxrs-jersey"
            d.modelPackage=c
            d.addAdditionalProperty("serializableModel","true")
            d.addAdditionalProperty("performBeanValidation","true")
            d.addAdditionalProperty("useBeanValidation","true")
            d.addAdditionalProperty("dateLibrary","java8")
            System.setProperty("models","")
            System.setProperty("modelDocs","false")
            val e=d.toClientOptInput()
            println(e.opts)
            val g=emptyList<io.swagger.v3.parser.core.models.AuthorizationValue>()
            val h=io.swagger.v3.parser.core.models.ParseOptions()
            h.setFlatten(true); h.setResolve(true)
            val i=io.swagger.parser.OpenAPIParser().readLocation(f.absolutePath,g,h).openAPI
            e.opts(io.swagger.codegen.v3.ClientOpts()).openAPI(i)
            io.swagger.codegen.v3.DefaultGenerator().opts(e).generate()
        }
        delete(fileTree(b).matching{include("**/InlineResponse*")})
    }
}
