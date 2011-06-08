package net.lucasward.grails.plugin

import org.hibernate.Session;
import org.hibernate.SessionFactory
import org.hibernate.envers.AuditReaderFactory
import org.hibernate.envers.query.AuditEntity
import org.hibernate.envers.query.AuditQuery
import org.hibernate.envers.query.AuditQueryCreator
import org.hibernate.envers.query.criteria.AggregatedAuditExpression

class GetRevisionEntityQuery {

	SessionFactory sessionFactory
	Class clazz

	GetRevisionEntityQuery(SessionFactory sessionFactory, Class clazz) {
		this.sessionFactory = sessionFactory
		this.clazz = clazz
	}

	private def query(instance) {
		Session session = sessionFactory.getCurrentSession()
		AggregatedAuditExpression latest = AuditEntity.revisionNumber().maximize()
		AuditQueryCreator qc = AuditReaderFactory.get(session).createQuery()
		AuditQuery query = qc.forRevisionsOfEntity(clazz, false, true)
		query.add(latest.add(AuditEntity.id().eq(instance.id)))
		def res = query.getResultList()
		if (res.size() == 0) return null
		res[0]
	}
	
	def queryEntity(instance) {
		query(instance)?.getAt(1)
	}
	def queryType(instance) {
		query(instance)?.getAt(2)
	}
}
