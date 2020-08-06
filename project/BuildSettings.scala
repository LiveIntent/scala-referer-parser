/**
 * Copyright 2012-2020 Snowplow Analytics Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Sbt
import sbt._
import Keys._

//Scaladocs
import sbtunidoc.ScalaUnidocPlugin.autoImport._
import com.typesafe.sbt.site.SitePlugin.autoImport._
import com.typesafe.sbt.SbtGit.GitKeys._

// Scoverage
import scoverage.ScoverageKeys._

import java.util.Date

object BuildSettings {

  lazy val javaCompilerOptions = Seq(
    "-source", "1.8",
    "-target", "1.8"
)

  private val artifactoryHost = "liveintent.jfrog.io"
  val snapshotsRepo = settingKey[Resolver]("LiveIntent Snapshots Repository")
  val releasesRepo = settingKey[Resolver]("LiveIntent Releases Repository")

  lazy val publishSettings = Seq(
    snapshotsRepo := "Artifactory Realm" at s"https://$artifactoryHost/liveintent/berlin;build.timestamp=${new Date().getTime}",
    releasesRepo := "Artifactory Realm" at s"https://$artifactoryHost/liveintent/berlin",
    publishTo := {
      if (isSnapshot.value) Some(snapshotsRepo.value)
      else Some(releasesRepo.value)
    },
    resolvers += "Artifactory" at s"https://$artifactoryHost/liveintent/berlin/",

    publishMavenStyle := true,
	publishArtifact := true,
	publishArtifact in Test := false,
	licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html")),
	pomIncludeRepository := { _ => false },
	homepage := Some(url("http://snowplowanalytics.com")),
	scmInfo := Some(ScmInfo(url("https://github.com/snowplow-referer-parser/scala-referer-parser"),
      "scm:git@github.com:snowplow-referer-parser/scala-referer-parser.git")),
	pomExtra := (
      <developers>
        <developer>
          <name>Snowplow Analytics Ltd</name>
            <email>support@snowplowanalytics.com</email>
            <organization>Snowplow Analytics Ltd</organization>
            <organizationUrl>http://snowplowanalytics.com</organizationUrl>
        </developer>
      </developers>)
  )

  lazy val docSettings = Seq(
    addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), siteSubdirName in ScalaUnidoc),
    gitRemoteRepo := "https://github.com/snowplow-referer-parser/scala-referer-parser.git",
    siteSubdirName := ""
  )
}
