from django.db import models

# Create your models here.
class Users(models.Model):
    u_id=models.IntegerField(primary_key=True)
    username=models.CharField(max_length=255)
    password=models.CharField(max_length=255)
    name=models.CharField(max_length=255)
    address=models.CharField(max_length=255)
    user_type=models.CharField(max_length=255)
    email=models.CharField(max_length=255)
    p_key=models.CharField(max_length=255)
    _p_key=models.CharField(max_length=255)


class Purchase_details(models.Model):
    p_id=models.IntegerField(primary_key=True)
    username=models.CharField(max_length=255)
    product_name=models.CharField(max_length=255)
    quantity=models.CharField(max_length=255)
    total_amount=models.CharField(max_length=255)
    status=models.CharField(max_length=255,default='Pending')
