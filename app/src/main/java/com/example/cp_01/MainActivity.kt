package com.example.cp_01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.net.Uri
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val renderableSource = RenderableSource.builder()
            .setSource(
                this,
                Uri.parse("assets/donut.glb"),
        RenderableSource.SourceType.GLB
        )
        .setRecenterMode(RenderableSource.RecenterMode.ROOT)
            .build()

        var renderable: ModelRenderable? = null

        ModelRenderable.builder()
            .setSource(
                this,
                renderableSource // This is the model we built earlier
            )
            .build()
            .thenAccept { modelRenderable ->
                renderable = modelRenderable // Store in a variable to reference later
            }
            .exceptionally { error ->
            // Handle errors here
                null
            }

        val fragment = supportFragmentManager.findFragmentById(R.id.ux_fragment)
                    as ArFragment

        fragment.setOnTapArPlaneListener { hitResult, _, _ ->
            val anchorNode = AnchorNode(hitResult.createAnchor())
            anchorNode.setParent(fragment.arSceneView.scene)

            val transformableNode = TransformableNode(fragment.transformationSystem)
            transformableNode.renderable = renderable // This is the built ModelRenderable
            transformableNode.setParent(anchorNode)
            transformableNode.select()
        }

    }
}