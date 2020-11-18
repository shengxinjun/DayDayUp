package com.sxj.entity;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;


@RelationshipEntity(type = "belong")
public class Relation {
	@GraphId
    private Long id;

    @StartNode
    private Product source;

    @EndNode
    private Vendor target;

    @Property
    private String relationName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getSource() {
		return source;
	}

	public void setSource(Product source) {
		this.source = source;
	}

	public Vendor getTarget() {
		return target;
	}

	public void setTarget(Vendor target) {
		this.target = target;
	}

	public String getRelationName() {
		return relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
}
