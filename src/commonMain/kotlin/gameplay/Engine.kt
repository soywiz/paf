package gameplay


import com.soywiz.klock.milliseconds
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.time.delay
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.BmpSlice
import com.soywiz.korio.async.async
import com.soywiz.korio.async.launchAsap
import com.soywiz.korio.async.launchImmediately
import resources.Resources


suspend fun Container.frame() {
    delay(41.milliseconds)
}

fun getImage(graph:Int): BmpSlice {
    return Resources.pafAtlas["${graph.toString().padStart(3, '0')}.png"].texture
}

private lateinit var currentScene:Scene

abstract class SceneBase:Scene()
{
    init {
        currentScene = this
    }
}

abstract class Process(parent: Container) : Container() {

    init {
        parent.addChild(this)
        currentScene.launchAsap {
            main()
        }
    }

    open suspend fun main() {}

    suspend fun loop(block:suspend ()->Unit) {
        while(true) {
            block()
        }
    }

    suspend fun <T>async(callback: suspend () -> T) = currentScene.async(callback)

}



fun Container.foto(graph:Int, x:Int ,y:Int ,size_x:Int , z:Int, flags:Int): Image {
    return image(getImage(graph)){
        position(x,y)
        anchor(0.5, 0.5)
        scale(size_x/100.0, size_x/100.0)
        smoothing = false
    }
}

fun Scene.loop(block:suspend ()->Unit){
    launchImmediately {
        while(true) {
            block()
        }
    }
}

