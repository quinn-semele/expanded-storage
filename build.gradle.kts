plugins {
    id("com.github.jakemarsden.git-hooks") version "0.0.2"
}

gitHooks {
    hooks = mapOf(
        "pre-commit" to "checkLicenses"
    )
}