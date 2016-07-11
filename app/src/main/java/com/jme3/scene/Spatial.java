/*
 * Copyright (c) 2009-2013 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.scene;

import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.Collidable;
import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

/**
 * <code>Spatial</code> defines the base class for scene graph nodes. It
 * maintains a link to a parent, it's local transforms and the world's
 * transforms. All other scene graph elements, such as {@link Node} and
 * {@link Geometry} are subclasses of <code>Spatial</code>.
 *
 * @author Mark Powell
 * @author Joshua Slack
 * @version $Revision: 4075 $, $Data$
 */
public abstract class Spatial implements Cloneable, Collidable{

private static final Logger logger = Logger.getLogger( Spatial.class.getName() );

/**
 * Specifies how frustum culling should be handled by
 * this spatial.
 */
public enum CullHint{

	/**
	 * Do whatever our parent does. If no parent, default to {@link #Dynamic}.
	 */
	Inherit,
	/**
	 * Do not onDrawFrame if we are not at least partially within the view frustum
	 * of the camera. This is determined via the defined
	 * Camera planes whether or not this Spatial should be culled.
	 */
	Dynamic,
	/**
	 * Always cull this from the view, throwing away this object
	 * and any children from rendering commands.
	 */
	Always,
	/**
	 * Never cull this from view, always onDrawFrame it.
	 * Note that we will still get culled if our parent is culled.
	 */
	Never;
}

/**
 * Specifies if this spatial should be batched
 */
public enum BatchHint{

	/**
	 * Do whatever our parent does. If no parent, default to {@link #Always}.
	 */
	Inherit,
	/**
	 * This spatial will always be batched when attached to a BatchNode.
	 */
	Always,
	/**
	 * This spatial will never be batched when attached to a BatchNode.
	 */
	Never;
}

/**
 * Refresh flag types
 */
protected static final int RF_TRANSFORM = 0x01, // need light resort + combine transforms
				RF_BOUND = 0x02, RF_LIGHTLIST = 0x04; // changes in light lists

protected CullHint cullHint = CullHint.Inherit;
protected BatchHint batchHint = BatchHint.Inherit;
/**
 * Spatial's bounding volume relative to the world.
 */
protected BoundingVolume worldBound;

/**
 * This spatial's name.
 */
protected String name;
// scale values
public transient float queueDistance = Float.NEGATIVE_INFINITY;
protected Transform localTransform;
protected Transform worldTransform;


/**
 * Spatial's parent, or null if it has none.
 */
protected transient Node parent;
/**
 * Refresh flags. Indicate what data of the spatial need to be
 * updated to reflect the correct state.
 */
protected transient int refreshFlags = 0;


/**
 * Constructor instantiates a new <code>Spatial</code> object setting the
 * rotation, translation and scale value to defaults.
 *
 * @param name the name of the scene element. This is required for
 *             identification and comparison purposes.
 */
public Spatial( String name ){
	//        this();
	this.name = name;
}

/**
 * Indicate that the transform of this spatial has changed and that
 * a refresh is required.
 */
protected void setTransformRefresh(){
	refreshFlags |= RF_TRANSFORM;
	setBoundRefresh();
}

protected void setLightListRefresh(){
	refreshFlags |= RF_LIGHTLIST;
}

/**
 * Indicate that the bounding of this spatial has changed and that
 * a refresh is required.
 */
protected void setBoundRefresh(){
	refreshFlags |= RF_BOUND;

	Spatial p = parent;
	while ( p != null ) {
		if ( ( p.refreshFlags & RF_BOUND ) != 0 ) {
			return;
		}

		p.refreshFlags |= RF_BOUND;
		p = p.parent;
	}
}

/**
 * (Internal use only) Forces a refresh of the given types of data.
 *
 * @param transforms Refresh world transform based on parents'
 * @param bounds     Refresh bounding volume data based on child nodes
 * @param lights     Refresh light list based on parents'
 */
public void forceRefresh( boolean transforms, boolean bounds, boolean lights ){
	if ( transforms ) {
		setTransformRefresh();
	}
	if ( bounds ) {
		setBoundRefresh();
	}
	if ( lights ) {
		setLightListRefresh();
	}
}

/**
 * Sets the name of this spatial.
 *
 * @param name The spatial's new name.
 */
public void setName( String name ){
	this.name = name;
}

/**
 * Returns the name of this spatial.
 *
 * @return This spatial's name.
 */
public String getName(){
	return name;
}

/**
 * <code>getWorldRotation</code> retrieves the absolute rotation of the
 * Spatial.
 *
 * @return the Spatial's world rotation quaternion.
 */
public Quaternion getWorldRotation(){
	checkDoTransformUpdate();
	return worldTransform.getRotation();
}

/**
 * <code>getWorldTranslation</code> retrieves the absolute translation of
 * the spatial.
 *
 * @return the Spatial's world tranlsation vector.
 */
public Vector3f getWorldTranslation(){
	checkDoTransformUpdate();
	return worldTransform.getTranslation();
}

/**
 * <code>getWorldScale</code> retrieves the absolute scale factor of the
 * spatial.
 *
 * @return the Spatial's world scale factor.
 */
public Vector3f getWorldScale(){
	checkDoTransformUpdate();
	return worldTransform.getScale();
}

/**
 * <code>getWorldTransform</code> retrieves the world transformation
 * of the spatial.
 *
 * @return the world transform.
 */
public Transform getWorldTransform(){
	checkDoTransformUpdate();
	return worldTransform;
}

/**
 * <code>rotateUpTo</code> is a utility function that alters the
 * local rotation to point the Y axis in the direction given by newUp.
 *
 * @param newUp the up vector to use - assumed to be a unit vector.
 */
public void rotateUpTo( Vector3f newUp ){
	TempVars vars = TempVars.get();

	Vector3f compVecA = vars.vect1;
	Quaternion q = vars.quat1;

	// First figure out the current up vector.
	Vector3f upY = compVecA.set( Vector3f.UNIT_Y );
	Quaternion rot = localTransform.getRotation();
	rot.multLocal( upY );

	// get angle between vectors
	float angle = upY.angleBetween( newUp );

	// figure out rotation axis by taking cross product
	Vector3f rotAxis = upY.crossLocal( newUp ).normalizeLocal();

	// Build a rotation quat and apply current local rotation.
	q.fromAngleNormalAxis( angle, rotAxis );
	q.mult( rot, rot );

	vars.release();

	setTransformRefresh();
}

/**
 * <code>lookAt</code> is a convenience method for auto-setting the local
 * rotation based on a position in world space and an up vector. It computes the rotation
 * to transform the z-axis to point onto 'position' and the y-axis to 'up'.
 * Unlike {@link Quaternion#lookAt(com.jme3.math.Vector3f, com.jme3.math.Vector3f) }
 * this method takes a world position to look at and not a relative direction.
 * <p/>
 * Note : 28/01/2013 this method has been fixed as it was not taking into account the parent rotation.
 * This was resulting in improper rotation when the spatial had rotated parent nodes.
 * This method is intended to work in world space, so no matter what parent graph the
 * spatial has, it will look at the given position in world space.
 *
 * @param position where to look at in terms of world coordinates
 * @param upVector a vector indicating the (local) up direction. (typically {0,
 *                 1, 0} in jME.)
 */
public void lookAt( Vector3f position, Vector3f upVector ){
	Vector3f worldTranslation = getWorldTranslation();

	TempVars vars = TempVars.get();

	Vector3f compVecA = vars.vect4;

	compVecA.set( position ).subtractLocal( worldTranslation );
	getLocalRotation().lookAt( compVecA, upVector );

	if ( getParent() != null ) {
		Quaternion rot = vars.quat1;
		rot = rot.set( parent.getWorldRotation() ).inverseLocal().multLocal( getLocalRotation() );
		rot.normalizeLocal();
		setLocalRotation( rot );
	}
	vars.release();
	setTransformRefresh();
}

/**
 * Should be overridden by Node and Geometry.
 */
protected void updateWorldBound(){
	// the world bound of a leaf is the same as it's model bound
	// for a node, the world bound is a combination of all it's children
	// bounds
	// -> handled by subclass
	refreshFlags &= ~RF_BOUND;
}


/**
 * Should only be called from updateGeometricState().
 * In most cases should not be subclassed.
 */
protected void updateWorldTransforms(){
	if ( parent == null ) {
		worldTransform.set( localTransform );
		refreshFlags &= ~RF_TRANSFORM;
	} else {
		// check if transform for parent is updated
		worldTransform.set( localTransform );
		worldTransform.combineWithParent( parent.worldTransform );
		refreshFlags &= ~RF_TRANSFORM;
	}
}

/**
 * Computes the world transform of this Spatial in the most
 * efficient manner possible.
 */
void checkDoTransformUpdate(){
	if ( ( refreshFlags & RF_TRANSFORM ) == 0 ) {
		return;
	}

	if ( parent == null ) {
		worldTransform.set( localTransform );
		refreshFlags &= ~RF_TRANSFORM;
	} else {
		TempVars vars = TempVars.get();

		Spatial[] stack = vars.spatialStack;
		Spatial rootNode = this;
		int i = 0;
		while ( true ) {
			Spatial hisParent = rootNode.parent;
			if ( hisParent == null ) {
				rootNode.worldTransform.set( rootNode.localTransform );
				rootNode.refreshFlags &= ~RF_TRANSFORM;
				i--;
				break;
			}

			stack[ i ] = rootNode;

			if ( ( hisParent.refreshFlags & RF_TRANSFORM ) == 0 ) {
				break;
			}

			rootNode = hisParent;
			i++;
		}

		vars.release();

		for ( int j = i; j >= 0; j-- ) {
			rootNode = stack[ j ];
			//rootNode.worldTransform.set(rootNode.localTransform);
			//rootNode.worldTransform.combineWithParent(rootNode.parent.worldTransform);
			//rootNode.refreshFlags &= ~RF_TRANSFORM;
			rootNode.updateWorldTransforms();
		}
	}
}

/**
 * Computes this Spatial's world bounding volume in the most efficient
 * manner possible.
 */
void checkDoBoundUpdate(){
	if ( ( refreshFlags & RF_BOUND ) == 0 ) {
		return;
	}

	checkDoTransformUpdate();

	// Go to children recursively and update their bound
	if ( this instanceof Node ) {
		Node node = (Node) this;
		int len = node.getQuantity();
		for ( int i = 0; i < len; i++ ) {
			Spatial child = node.getChild( i );
			child.checkDoBoundUpdate();
		}
	}

	// All children's bounds have been updated. Update my own now.
	updateWorldBound();
}


/**
 * Convert a vector (in) from this spatials' local coordinate space to world
 * coordinate space.
 *
 * @param in    vector to read from
 * @param store where to write the result (null to create a new vector, may be
 *              same as in)
 * @return the result (store)
 */
public Vector3f localToWorld( final Vector3f in, Vector3f store ){
	checkDoTransformUpdate();
	return worldTransform.transformVector( in, store );
}

/**
 * Convert a vector (in) from world coordinate space to this spatials' local
 * coordinate space.
 *
 * @param in    vector to read from
 * @param store where to write the result
 * @return the result (store)
 */
public Vector3f worldToLocal( final Vector3f in, final Vector3f store ){
	checkDoTransformUpdate();
	return worldTransform.transformInverseVector( in, store );
}

/**
 * <code>getParent</code> retrieves this node's parent. If the parent is
 * null this is the root node.
 *
 * @return the parent of this node.
 */
public Node getParent(){
	return parent;
}

/**
 * Called by {@link Node#attachChild(Spatial)} and
 * {@link Node#detachChild(Spatial)} - don't call directly.
 * <code>setParent</code> sets the parent of this node.
 *
 * @param parent the parent of this node.
 */
protected void setParent( Node parent ){
	this.parent = parent;
}

/**
 * <code>removeFromParent</code> removes this Spatial from it's parent.
 *
 * @return true if it has a parent and performed the remove.
 */
public boolean removeFromParent(){
	if ( parent != null ) {
		parent.detachChild( this );
		return true;
	}
	return false;
}

/**
 * determines if the provided Node is the parent, or parent's parent, etc. of this Spatial.
 *
 * @param ancestor the ancestor object to look for.
 * @return true if the ancestor is found, false otherwise.
 */
public boolean hasAncestor( Node ancestor ){
	if ( parent == null ) {
		return false;
	} else if ( parent.equals( ancestor ) ) {
		return true;
	} else {
		return parent.hasAncestor( ancestor );
	}
}

/**
 * <code>getLocalRotation</code> retrieves the local rotation of this
 * node.
 *
 * @return the local rotation of this node.
 */
public Quaternion getLocalRotation(){
	return localTransform.getRotation();
}

/**
 * <code>setLocalRotation</code> sets the local rotation of this node
 * by using a {@link Matrix3f}.
 *
 * @param rotation the new local rotation.
 */
public void setLocalRotation( Matrix3f rotation ){
	localTransform.getRotation().fromRotationMatrix( rotation );
	setTransformRefresh();
}

/**
 * <code>setLocalRotation</code> sets the local rotation of this node.
 *
 * @param quaternion the new local rotation.
 */
public void setLocalRotation( Quaternion quaternion ){
	localTransform.setRotation( quaternion );
	setTransformRefresh();
}

/**
 * <code>getLocalScale</code> retrieves the local scale of this node.
 *
 * @return the local scale of this node.
 */
public Vector3f getLocalScale(){
	return localTransform.getScale();
}

/**
 * <code>setLocalScale</code> sets the local scale of this node.
 *
 * @param localScale the new local scale, applied to x, y and z
 */
public void setLocalScale( float localScale ){
	localTransform.setScale( localScale );
	setTransformRefresh();
}

/**
 * <code>setLocalScale</code> sets the local scale of this node.
 */
public void setLocalScale( float x, float y, float z ){
	localTransform.setScale( x, y, z );
	setTransformRefresh();
}

/**
 * <code>setLocalScale</code> sets the local scale of this node.
 *
 * @param localScale the new local scale.
 */
public void setLocalScale( Vector3f localScale ){
	localTransform.setScale( localScale );
	setTransformRefresh();
}

/**
 * <code>getLocalTranslation</code> retrieves the local translation of
 * this node.
 *
 * @return the local translation of this node.
 */
public Vector3f getLocalTranslation(){
	return localTransform.getTranslation();
}

/**
 * <code>setLocalTranslation</code> sets the local translation of this
 * spatial.
 *
 * @param localTranslation the local translation of this spatial.
 */
public void setLocalTranslation( Vector3f localTranslation ){
	this.localTransform.setTranslation( localTranslation );
	setTransformRefresh();
}

/**
 * <code>setLocalTranslation</code> sets the local translation of this
 * spatial.
 */
public void setLocalTranslation( float x, float y, float z ){
	this.localTransform.setTranslation( x, y, z );
	setTransformRefresh();
}

/**
 * <code>setLocalTransform</code> sets the local transform of this
 * spatial.
 */
public void setLocalTransform( Transform t ){
	this.localTransform.set( t );
	setTransformRefresh();
}

/**
 * <code>getLocalTransform</code> retrieves the local transform of
 * this spatial.
 *
 * @return the local transform of this spatial.
 */
public Transform getLocalTransform(){
	return localTransform;
}

/**
 * Translates the spatial by the given translation vector.
 *
 * @return The spatial on which this method is called, e.g <code>this</code>.
 */
public Spatial move( float x, float y, float z ){
	this.localTransform.getTranslation().addLocal( x, y, z );
	setTransformRefresh();

	return this;
}

/**
 * Translates the spatial by the given translation vector.
 *
 * @return The spatial on which this method is called, e.g <code>this</code>.
 */
public Spatial move( Vector3f offset ){
	this.localTransform.getTranslation().addLocal( offset );
	setTransformRefresh();

	return this;
}

/**
 * Scales the spatial by the given value
 *
 * @return The spatial on which this method is called, e.g <code>this</code>.
 */
public Spatial scale( float s ){
	return scale( s, s, s );
}

/**
 * Scales the spatial by the given scale vector.
 *
 * @return The spatial on which this method is called, e.g <code>this</code>.
 */
public Spatial scale( float x, float y, float z ){
	this.localTransform.getScale().multLocal( x, y, z );
	setTransformRefresh();

	return this;
}

/**
 * Rotates the spatial by the given rotation.
 *
 * @return The spatial on which this method is called, e.g <code>this</code>.
 */
public Spatial rotate( Quaternion rot ){
	this.localTransform.getRotation().multLocal( rot );
	setTransformRefresh();

	return this;
}

/**
 * Rotates the spatial by the xAngle, yAngle and zAngle angles (in radians),
 * (aka pitch, yaw, roll) in the local coordinate space.
 *
 * @return The spatial on which this method is called, e.g <code>this</code>.
 */
public Spatial rotate( float xAngle, float yAngle, float zAngle ){
	TempVars vars = TempVars.get();
	Quaternion q = vars.quat1;
	q.fromAngles( xAngle, yAngle, zAngle );
	rotate( q );
	vars.release();

	return this;
}

/**
 * Centers the spatial in the origin of the world bound.
 *
 * @return The spatial on which this method is called, e.g <code>this</code>.
 */
public Spatial center(){
	Vector3f worldTrans = getWorldTranslation();
	Vector3f worldCenter = getWorldBound().getCenter();

	Vector3f absTrans = worldTrans.subtract( worldCenter );
	setLocalTranslation( absTrans );

	return this;
}

/**
 * @return the cull mode of this spatial, or if set to CullHint.Inherit,
 * the cullmode of it's parent.
 * @see #setCullHint(CullHint)
 */
public CullHint getCullHint(){
	if ( cullHint != CullHint.Inherit ) {
		return cullHint;
	} else if ( parent != null ) {
		return parent.getCullHint();
	} else {
		return CullHint.Dynamic;
	}
}

public BatchHint getBatchHint(){
	if ( batchHint != BatchHint.Inherit ) {
		return batchHint;
	} else if ( parent != null ) {
		return parent.getBatchHint();
	} else {
		return BatchHint.Always;
	}
}


/**
 * Sets the level of detail to use when rendering this Spatial,
 * this call propagates to all geometries under this Spatial.
 *
 * @param lod The lod level to set.
 */
public void setLodLevel( int lod ){
}

/**
 * <code>updateModelBound</code> recalculates the bounding object for this
 * Spatial.
 */
public abstract void updateModelBound();

/**
 * <code>setModelBound</code> sets the bounding object for this Spatial.
 *
 * @param modelBound the bounding object for this spatial.
 */
public abstract void setModelBound( BoundingVolume modelBound );

/**
 * @return The sum of all verticies under this Spatial.
 */
public abstract int getVertexCount();

/**
 * @return The sum of all triangles under this Spatial.
 */
public abstract int getTriangleCount();


/**
 * Note that we are <i>matching</i> the pattern, therefore the pattern
 * must match the entire pattern (i.e. it behaves as if it is sandwiched
 * between "^" and "$").
 * You can set regex modes, like case insensitivity, by using the (?X)
 * or (?X:Y) constructs.
 *
 * @param spatialSubclass Subclass which this must implement.
 *                        Null causes all Spatials to qualify.
 * @param nameRegex       Regular expression to match this name against.
 *                        Null causes all Names to qualify.
 * @return true if this implements the specified class and this's name
 * matches the specified pattern.
 * @see java.util.regex.Pattern
 */
public boolean matches( Class<? extends Spatial> spatialSubclass, String nameRegex ){
	if ( spatialSubclass != null && !spatialSubclass.isInstance( this ) ) {
		return false;
	}

	if ( nameRegex != null && ( name == null || !name.matches( nameRegex ) ) ) {
		return false;
	}

	return true;
}

/**
 * <code>getWorldBound</code> retrieves the world bound at this node
 * level.
 *
 * @return the world bound at this level.
 */
public BoundingVolume getWorldBound(){
	checkDoBoundUpdate();
	return worldBound;
}

/**
 * <code>setCullHint</code> alters how view frustum culling will treat this
 * spatial.
 *
 * @param hint one of: <code>CullHint.Dynamic</code>,
 *             <code>CullHint.Always</code>, <code>CullHint.Inherit</code>, or
 *             <code>CullHint.Never</code>
 *             <p/>
 *             The effect of the default value (CullHint.Inherit) may change if the
 *             spatial gets re-parented.
 */
public void setCullHint( CullHint hint ){
	cullHint = hint;
}

/**
 * <code>setBatchHint</code> alters how batching will treat this spatial.
 *
 * @param hint one of: <code>BatchHint.Never</code>,
 *             <code>BatchHint.Always</code>, or <code>BatchHint.Inherit</code>
 *             <p/>
 *             The effect of the default value (BatchHint.Inherit) may change if the
 *             spatial gets re-parented.
 */
public void setBatchHint( BatchHint hint ){
	batchHint = hint;
}

/**
 * @return the cullmode set on this Spatial
 */
public CullHint getLocalCullHint(){
	return cullHint;
}

/**
 * @return the batchHint set on this Spatial
 */
public BatchHint getLocalBatchHint(){
	return batchHint;
}

/**
 * Returns the Spatial's name followed by the class of the spatial <br>
 * Example: "MyNode (com.jme3.scene.Spatial)
 *
 * @return Spatial's name followed by the class of the Spatial
 */
@Override
public String toString(){
	return name + " (" + this.getClass().getSimpleName() + ')';
}

/**
 * Creates a transform matrix that will convert from this spatials'
 * local coordinate space to the world coordinate space
 * based on the world transform.
 *
 * @param store Matrix where to store the result, if null, a new one
 *              will be created and returned.
 * @return store if not null, otherwise, a new matrix containing the result.
 * @see Spatial#getWorldTransform()
 */
public Matrix4f getLocalToWorldMatrix( Matrix4f store ){
	if ( store == null ) {
		store = new Matrix4f();
	} else {
		store.loadIdentity();
	}
	// multiply with scale first, then rotate, finally translate (cf.
	// Eberly)
	store.scale( getWorldScale() );
	store.multLocal( getWorldRotation() );
	store.setTranslation( getWorldTranslation() );
	return store;
}

/**
 * Visit each scene graph element ordered by DFS
 *
 * @param visitor
 */
public abstract void depthFirstTraversal( SceneGraphVisitor visitor );

/**
 * Visit each scene graph element ordered by BFS
 *
 * @param visitor
 */
public void breadthFirstTraversal( SceneGraphVisitor visitor ){
	Queue<Spatial> queue = new LinkedList<Spatial>();
	queue.add( this );

	while ( !queue.isEmpty() ) {
		Spatial s = queue.poll();
		visitor.visit( s );
		s.breadthFirstTraversal( visitor, queue );
	}
}

protected abstract void breadthFirstTraversal( SceneGraphVisitor visitor, Queue<Spatial> queue );
}
