package ${PackageTop}.${PackageProject}.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import ${PackageTop}.${PackageCommons}.entity.BaseEntity;

@Entity
@Table(name="${TablePrefix}${EntityName}")
@Cache(usage=CacheConcurrencyStrategy.${SecondCache})
public class ${EntityName} extends BaseEntity{
<#list FieldList as var>
  <#if var[0]=="Column">
	private ${var[1]} ${var[2]};	//${var[3]}
  <#elseif var[0]=="ManyToOne">
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(CascadeType.SAVE_UPDATE)
	@JoinColumn(name="${var[2]}id")
	private ${var[1]} ${var[2]};	//${var[3]}
  <#elseif var[0]=="OneToMany">
	@OneToMany(mappedBy=${var[1]}类中的${EntityName}变量名,fetch=FetchType.EAGER)
	@Cascade(CascadeType.ALL)
	private List<${var[1]}> ${var[2]};	//${var[3]}
  <#elseif var[0]=="OneToOne">
  <#elseif var[0]=="ManyToMany">
	
	</#if>
</#list>
<#list FieldList as var>
	
<#if var[0]=="Column">
	public ${var[1]} get${var[2]?cap_first}() {
		return ${var[2]};
	}
	public void set${var[2]?cap_first}(${var[1]} ${var[2]}) {
		this.${var[2]} = ${var[2]};
	}
<#elseif var[0]=="ManyToOne">
	public ${var[1]} get${var[2]?cap_first}() {
		return ${var[2]};
	}
	public void set${var[2]?cap_first}(${var[1]} ${var[2]}) {
		this.${var[2]} = ${var[2]};
	}
<#elseif var[0]=="OneToMany">
	public List<${var[1]}> get${var[2]?cap_first}() {
		return ${var[2]};
	}
	public void set${var[2]?cap_first}(List<${var[1]}> ${var[2]}) {
		this.${var[2]} = ${var[2]};
	}
<#elseif var[0]=="OneToOne">
<#elseif var[0]=="ManyToMany">
	
	</#if>
</#list>
<#list FieldList as var>
	
</#list>
	
}
<#function avg x y>
    <#return (x + y) / 2>
</#function>