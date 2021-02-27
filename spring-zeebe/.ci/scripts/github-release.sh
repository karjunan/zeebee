#!/bin/bash -xeu

export GITHUB_TOKEN=${GITHUB_TOKEN_PSW}
export GITHUB_ORG=zeebe-io
export GITHUB_REPO=spring-zeebe

curl -sL https://github.com/aktau/github-release/releases/download/v0.7.2/linux-amd64-github-release.tar.bz2 | tar xjvf - --strip 3
GITHUB_RELEASE=${PWD}/github-release

${GITHUB_RELEASE} release --user ${GITHUB_ORG} --repo ${GITHUB_REPO} --tag ${RELEASE_VERSION} --draft --name "Spring Zeebe ${RELEASE_VERSION}" --description ""

function upload {
  pushd ${1}

  local artifact=${2}
  local checksum=${artifact}.sha1sum

  sha1sum ${artifact} > ${checksum}

  ${GITHUB_RELEASE} upload --user ${GITHUB_ORG} --repo ${GITHUB_REPO} --tag ${RELEASE_VERSION} --name "${artifact}" --file "${artifact}"
  ${GITHUB_RELEASE} upload --user ${GITHUB_ORG} --repo ${GITHUB_REPO} --tag ${RELEASE_VERSION} --name "${checksum}" --file "${checksum}"

  popd
}

upload client/spring-zeebe/target/ spring-zeebe-${RELEASE_VERSION}.jar
upload client/spring-zeebe-starter/target/ spring-zeebe-starter-${RELEASE_VERSION}.jar
