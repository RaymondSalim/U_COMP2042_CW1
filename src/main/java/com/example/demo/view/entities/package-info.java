/**
 * Contains the core classes representing game entities and mechanics.
 *
 * <p>
 * This package provides abstractions and implementations for various active objects in the game,
 * including planes, projectiles, and destructible actors. These entities form the basis of
 * game interactions and gameplay mechanics.
 * </p>
 *
 * <p>
 * Classes and interfaces in this package:
 * </p>
 * <ul>
 *     <li>{@link com.example.demo.view.entities.ActiveActor} - A base class for interactive game entities.</li>
 *     <li>{@link com.example.demo.view.entities.ActiveActorDestructible} - An active actor that can take damage and be destroyed.</li>
 *     <li>{@link com.example.demo.view.entities.Destructible} - An interface for objects that can be damaged or destroyed.</li>
 *     <li>{@link com.example.demo.view.entities.FighterPlane} - Represents a fighter plane capable of firing projectiles.</li>
 *     <li>{@link com.example.demo.view.entities.Projectile} - Represents a projectile fired by an actor.</li>
 * </ul>
 */
package com.example.demo.view.entities;