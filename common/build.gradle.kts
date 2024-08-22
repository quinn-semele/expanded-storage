plugins {
    id("multiloader-common")
}

sourceSets.main.configure {
    resources.srcDir("src/generated/resources")
}