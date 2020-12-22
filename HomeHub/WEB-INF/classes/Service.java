public class Service {

	int service_id;
  String service_provider_id;
  String service_name;
  String service_category_super;
  String service_category_sub;
  String service_images;
  String service_description;
  double service_rate;
  String service_status;
  String service_admin_status;

	public Service(
  int service_id,
  String service_provider_id,
  String service_name,
  String service_category_super,
  String service_category_sub,
  String service_images,
  String service_description,
  double service_rate,
  String service_status,
  String service_admin_status
  ) {
    this.service_id = service_id;
    this.service_provider_id = service_provider_id;
    this.service_name = service_name;
    this.service_category_super = service_category_super;
    this.service_category_sub = service_category_sub;
    this.service_images = service_images;
    this.service_description = service_description;
    this.service_rate = service_rate;
    this.service_status = service_status;
    this.service_admin_status = service_admin_status;
	}

}
