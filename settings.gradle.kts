rootProject.name = "spring-boot-extensible-demo"

include("modules:plugin-context", "modules:spring-plugin-context")
include("modules:my-plugin-1", "modules:my-plugin-2", "modules:my-plugin-3")

include("apps:spring-redis-demo")