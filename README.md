# Jersey Standalone

[![Build Status (Travis)](https://img.shields.io/travis/skapral/jersey-se/master.svg)](https://travis-ci.org/skapral/jersey-se)
[![Build status (AppVeyor)](https://ci.appveyor.com/api/projects/status/sumvi0c7teo9oq94?svg=true)](https://ci.appveyor.com/project/skapral/jersey-se)
[![Codecov](https://codecov.io/gh/skapral/jersey-se/branch/master/graph/badge.svg)](https://codecov.io/gh/skapral/jersey-se)

Compact self-sufficient JAX-RS distribution based on Grizzly and Jersey.

## Quick start

1. Add Maven dependency

```
<dependency>
    <groupId>com.github.skapral.jersey.se</groupId>
    <artifactId>jersey-se</artifactId>
    <version>x.y.z</version>
</dependency>
```

2. Define JAX-RS resource config

```
public class SimpleConfig extends ResourceConfig {
    /**
     * Ctor.
     */
    public SimpleConfig() {
        super(
            StatusEndpoint.class
        );
    }
}

@Path("status")
public class StatusEndpoint {
    /**
     * @return returns "OK"
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String status() {
        return "OK";
    }
}
```

3. Define entry point.

```
public static void main(String... args) throws Exception {
    new SrvGrizzlyWithJerseyAndJtwig(
        new Cp_PORT(),
        new StatusEndpoint()
    ).start();
    System.in.read(); // Server instance uses daemon threads, so hold
    // main thread until you need the server online.
}
```

4. Run the instance.