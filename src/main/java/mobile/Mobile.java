package mobile;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;


@Entity
public class Mobile
{

	public enum srv
	{
			MOBILE_PREPAID,
			MOBILE_POSTPAID
	};

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(unique=true, updatable=false)
	private String msisdn;

	@Column(updatable=false)
	private int customerIdOwner;

	@Column(updatable=false)
	private int customerIdUser;

  private srv serviceType;

	@Column(updatable=false)
	private long serviceStartDate;

	public void setId(long id)
	{
		this.id = id;
	}

	public long getId()
	{
		return id;
	}

	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
	public String getMsisdn()
	{
		return this.msisdn;
	}

	public void setCustomerIdOwner(int customerIdOwner)
	{
		this.customerIdOwner = customerIdOwner;
	}
	public int getCustomerIdOwner()
	{
		return customerIdOwner;
	}

	public void setCustomerIdUser(int customerIdUser)
	{
		this.customerIdUser = customerIdUser;
	}
	public int getCustomerIdUser()
	{
		return customerIdUser;
	}

	public void setServiceType(String serviceType)
	{
			if(serviceType.equals("MOBILE_PREPAID"))
				this.serviceType = srv.MOBILE_PREPAID;
			if(serviceType.equals("MOBILE_POSTPAID"))
				this.serviceType = srv.MOBILE_POSTPAID;
		}
	public srv getServiceType()
	{
			return serviceType;
		}

	public void setServiceStartDate(long serviceStartDate)
	{
		this.serviceStartDate = serviceStartDate;
	}
	public long getServiceStartDate()
	{
		return serviceStartDate;
	}

}
