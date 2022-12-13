# edge\_pseudonym\_file\_upload

This projects implements the server side of the upload of pseudonymized patient
data files.

You may find links to documentations of libraries and technologies
used in this and other **Lupus** POC components inside the
[README of the Lupus POC parent project](https://github.com/StefanHoening/ukd_lupus_parent)

## Project build and run

### Basic Maven Build and run


With JDK17+

```bash
mvn clean package
```

Builds the project jar file.

```bash
mvn clean install
```

Builds a docker image and pushes that image to `docker.io`. You have to provide a system property `docker.lupus_poc.namespace` for this to work.

```bash
java -jar target/edge_find_patient_adapter_db.jar
```

### Container image build and run

A container build and deployment to docker.io is included in the maven build
phase *install*.

You may run this container locally using the scripts in
`src/scripts/podman/run_container.sh`. This requires the previous creation of a **POD** named
`ukd_lupus_pod` as described inside the
[README of the Lupus POC parent project](https://github.com/StefanHoening/ukd_lupus_parent).

`run_container.sh` creates a container, copies the provided configuration file 
containing the database connection information into the container starts the container.

You may need to add an alias entry into your `/etc/containers/registries.conf.d/shortnames.conf`
to resolve the image name 'edge-fpa' from a container registry.


# edge_snomed_query
