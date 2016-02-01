package com.innvo.drools;

import java.util.Collection;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.ObjectFilter;
import org.drools.runtime.StatefulKnowledgeSession;
import com.innvo.domain.Score;

public class RuleExecutor {


	public Score processRules(Todo todo)
	{
		KnowledgeBase kbase = null;
		Score score=null;
		try {
			kbase = readKnowledgeBase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		ksession.insert(todo);
		ksession.fireAllRules();
		Collection<Object> result = findFacts(ksession, Score.class);
		Object[] objects = result.toArray();
		if(objects.length == 1)
		{
		score = (Score) objects[0];
		}
		return score;
	}

	private KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("rules/routeRuleSetOne.drl"), ResourceType.DRL);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}

	protected static Collection<Object> findFacts(final StatefulKnowledgeSession session, @SuppressWarnings("rawtypes") final Class factClass) {
		ObjectFilter filter = new ObjectFilter() {
			public boolean accept(Object object) {
				return object.getClass().equals(factClass);
			}
		};
		Collection<Object> results = session.getObjects(filter);
		return results;
	}

}
