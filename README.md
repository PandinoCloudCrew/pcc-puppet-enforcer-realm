# Code Health

| SonarCloud                                                                                                                                                                                                                                                             | CodeScene                                                                                                                            | Workflows                                                                                                                                                                                                                                  |
|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [![Quality Gate Status](https://sonar.ops.pandino.co/api/project_badges/measure?project=pcc-puppet-enforcer-realm&metric=alert_status&token=sqb_728ba6c59c6ba8ac8269f1528c19cf6538c592f7)](https://sonar.ops.pandino.co/summary/new_code?id=pcc-puppet-enforcer-realm) | [![CodeScene general](https://codescene.io/images/analyzed-by-codescene-badge.svg)](https://codescene.io/projects/29004)             | [![CodeQL](https://github.com/PandinoCloudCrew/pcc-puppet-enforcer-realm/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/PandinoCloudCrew/pcc-puppet-enforcer-realm/actions/workflows/codeql-analysis.yml)            |
| [![Coverage](https://sonar.ops.pandino.co/api/project_badges/measure?project=pcc-puppet-enforcer-realm&metric=coverage&token=sqb_728ba6c59c6ba8ac8269f1528c19cf6538c592f7)](https://sonar.ops.pandino.co/summary/new_code?id=pcc-puppet-enforcer-realm)                | [![CodeScene Code Health](https://codescene.io/projects/29004/status-badges/code-health)](https://codescene.io/projects/29004)       | [![Sonar Code Coverage](https://github.com/PandinoCloudCrew/pcc-puppet-enforcer-realm/actions/workflows/check-coverage.yml/badge.svg)](https://github.com/PandinoCloudCrew/pcc-puppet-enforcer-realm/actions/workflows/check-coverage.yml) |
| [![Technical Debt](https://sonar.ops.pandino.co/api/project_badges/measure?project=pcc-puppet-enforcer-realm&metric=sqale_index&token=sqb_728ba6c59c6ba8ac8269f1528c19cf6538c592f7)](https://sonar.ops.pandino.co/summary/new_code?id=pcc-puppet-enforcer-realm)       | [![CodeScene System Mastery](https://codescene.io/projects/29004/status-badges/system-mastery)](https://codescene.io/projects/29004) | [![Release](https://github.com/PandinoCloudCrew/pcc-puppet-enforcer-realm/actions/workflows/release.yml/badge.svg)](https://github.com/PandinoCloudCrew/pcc-puppet-enforcer-realm/actions/workflows/release.yml)                           |
| [![Code Smells](https://sonar.ops.pandino.co/api/project_badges/measure?project=pcc-puppet-enforcer-realm&metric=code_smells&token=sqb_728ba6c59c6ba8ac8269f1528c19cf6538c592f7)](https://sonar.ops.pandino.co/summary/new_code?id=pcc-puppet-enforcer-realm)          | [![CodeScene Missed Goals](https://codescene.io/projects/29004/status-badges/missed-goals)](https://codescene.io/projects/29004)     |                                                                                                                                                                                                                                            |
## Update dependencies

```bash
gradle dependencyUpdates -Drevision=release
```

### Setup

Add the following GitHub secrets:

| Name                   | Description                                                                                                          |
|------------------------|----------------------------------------------------------------------------------------------------------------------|
| DOCKER_USERNAME        | Username for Docker registry authentication.                                                                         |
| DOCKER_PASSWORD        | Docker registry password.                                                                                            |
| DOCKER_REPOSITORY_PATH | Path to the docker image repository inside the registry, e.g. for the image `foo/bar/micronaut:0.1` it is `foo/bar`. |
| DOCKER_REGISTRY_URL    | Docker registry url.                                                                                                 |


### Setup

Add the following GitHub secrets:

| Name                   | Description                                                                                                          |
|------------------------|----------------------------------------------------------------------------------------------------------------------|
| DOCKER_USERNAME        | Username for Docker registry authentication.                                                                         |
| DOCKER_PASSWORD        | Docker registry password.                                                                                            |
| DOCKER_REPOSITORY_PATH | Path to the docker image repository inside the registry, e.g. for the image `foo/bar/micronaut:0.1` it is `foo/bar`. |
| DOCKER_REGISTRY_URL    | Docker registry url.                                                                                                 |


# Vault Configuration

Requires a secret engine named `credentials`
Requires a secret engine named `kv`

# Keycloak Configuration

Requires that default-realm-roles is modified to add the realm roles `inpersonation`
and `manage-users`

Requires an existing client that has admin permissions to create other clients, groups and users.

Organization's Client is used to create it's users.