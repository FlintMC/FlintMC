package net.flintmc.minecraft.version

data class MinecraftVersion(
    var arguments: MutableMap<String, Any>,
    var assetIndex: AssetIndex,
    var assets: String,
    var downloads: MutableMap<String, Download>,
    var id: String,
    var libraries: MutableCollection<Library>,
    var logging: MutableMap<String, Any>,
    var mainClass: String,
    var minimumLauncherVersion: Int,
    var releaseTime: String,
    var time: String,
    var type: String
) {

    data class Download(var sha1: String, var size: Int, var url: String)

    data class AssetIndex(var id: String, var sha1: String, var size: Int, var totalSize: Int, var url: String)

    data class Library(
        var downloads: Download?,
        var extract: MutableMap<String, Array<String>>?,
        var name: String,
        var natives: MutableMap<String, String>?,
        var rules: MutableCollection<Rule>?
    ) {
        data class Download(var artifact: Artifact, var classifiers: MutableMap<String, Classifier>?) {
            data class Artifact(var path: String, var sha1: String, var size: Int, var url: String)

            data class Classifier(var path: String, var sha1: String, var size: Int, var url: String)
        }

        data class Rule(var action: String, var os: Os?) {
            data class Os(var name: String)
        }
    }
}