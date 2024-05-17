from django.shortcuts import render
from .models import *
import json
from django.core import serializers
from django.http import HttpResponse, JsonResponse
from django.db.models import Q
from django.db.models import Count
import re
import os
import cv2
from datetime import datetime, timedelta
import datetime
from threading import Thread
import threading
from datetime import date
from django.views.decorators.cache import never_cache
from django.core.files.storage import FileSystemStorage
import qrcode
from django.views.decorators.csrf import csrf_exempt
import hashlib
import hmac
import base64
import qrcode
import geocoder
import uuid
from typing import Tuple
from Crypto.PublicKey import ECC
from Crypto.Hash import SHA256
from geopy.distance import geodesic
from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
import base64
from Crypto.Util.Padding import pad ,unpad

@never_cache
def show_index(request):
	return render(request, "login.html", {})


@never_cache
def logout(request):
	if 'uid' in request.session:
		del request.session['uid']
	return render(request,'login.html')


def generate_qrcode(b_id,book_name):

	# Data to be encoded
	data = '%s'%(b_id)
	print("\n *************************")
	print("data : ",data)
	 
	# Encoding data using make() function
	img = qrcode.make(data)
	path= 'library_app/static/QR CODE/%s.png' %(book_name)
	# Saving as an image file
	img.save(path)


def generate_key_pair(user_identity):
	# Generate a key pair using ECC (Elliptic Curve Cryptography)
	private_key = ECC.generate(curve='P-256')
	
	# Hash the user identity (email in this case) using SHA256
	hashed_identity = SHA256.new(user_identity.encode()).digest()
	
	# Derive a public key from the private key and hashed identity
	public_key = private_key.pointQ * int.from_bytes(hashed_identity, 'big')
	
	return private_key, public_key


@csrf_exempt
def register(request):
	name=request.POST.get("name")
	username=request.POST.get("username")
	address=request.POST.get("address")
	age=request.POST.get("age")
	email=request.POST.get("email")
	password=request.POST.get("password")
	user_type=request.POST.get("utype")

	print(name)

	response_data={}
	# try:
	d = Users.objects.filter(username=username)
	c = d.count()
	if c == 1:
		response_data['msg'] = "Already registered"
	else:
		#Generate key
		private_key, public_key = generate_key_pair(email)
		private_key_hex = private_key.export_key(format='DER').hex()
		public_key_hex = private_key.public_key().export_key(format='DER').hex()  # Use public_key() to get the public key


		ob=Users(name=name,username=username,address=address,email=email,password=password,user_type=user_type,p_key=public_key_hex,_p_key=private_key_hex)
		ob.save()

		response_data['msg'] = "yes"
	# except:
	# 	response_data['msg'] = "no"
	return JsonResponse(response_data)

PRODUCT_PRICES = {
	'Coca Cola': 40,
	'Fanta': 35,
}


@csrf_exempt
def add_purchase(request):
	product_name=request.POST.get("productname")
	quantity=int(request.POST.get("quantity"))
	username=request.POST.get("username")
	print(product_name)
	print(quantity)
	print(type(quantity))
	response_data={}
	try:
		d = Purchase_details.objects.filter(username=username,product_name=product_name,status="Pending")
		c = d.count()
		if c == 1:
			response_data['msg'] = "Purchase request is in pending list"
		else:
			price = PRODUCT_PRICES.get(product_name)
			print("**********")
			print(price)
			print(type(price))
			# Calculate total amount
			total_amount = quantity * price
			print("Total :",total_amount)
			ob=Purchase_details(username=username,product_name=product_name,quantity=quantity,total_amount=total_amount)
			ob.save()

			response_data['msg'] = "yes"
	except:
		response_data['msg'] = "no"
	return JsonResponse(response_data)



@csrf_exempt
def find_login(request):
	username=request.POST.get("username")
	password=request.POST.get("password")

	try:
		ob=Users.objects.get(username=username,password=password)
		get_usertype=ob.user_type
		if get_usertype=="User":
			data={"msg":"User"}
		else:
			data={"msg":"Merchant"}
		return JsonResponse(data,safe=False)
	except:
		data={"msg":"no"}
		return JsonResponse(data,safe=False)



@csrf_exempt
def get_user_details(request):
 
	username=request.POST.get("username")
	resplist=[]
	respdata={}
	ob=[Users.objects.get(username=username)]

	resplist=[]
	respdata={}
	for i in ob:
		data={}
		data["username"]=i.username
		data["email"]=i.email
		data["address"]=i.address
		data["password"]=i.password
		data["age"]=i.age
		data["name"]=i.name

		resplist.append(data)

	respdata["data"]=resplist
	print(respdata)
	return JsonResponse(respdata,safe=False)


@csrf_exempt
def approve(request):
	p_id=request.POST.get("p_id")

	response_data={}
	obj1=Purchase_details.objects.get(p_id=p_id)
	obj1.status="Approved"
	obj1.save()

	response_data['msg'] = "yes"

	return JsonResponse(response_data)




@csrf_exempt
def get_purchase_request(request):

	resplist=[]
	respdata={}
	ob=Purchase_details.objects.filter(status="Pending")
	for j in ob:
		p_id=j.p_id
		username=j.username
		product_name=j.product_name
		quantity=j.quantity
		total_amount=j.total_amount
		status=j.status

		data={}
		data["p_id"]=p_id
		data["username"]=username
		data["product_name"]=product_name
		data["quantity"]=quantity
		data["total_amount"]=total_amount
		data["status"]=status

		resplist.append(data)
	print(resplist)

	respdata["data"]=resplist
	print(respdata)
	return JsonResponse(respdata,safe=False)


@csrf_exempt
def get_accepted_purchases(request):

	username=request.POST.get("username")
	resplist=[]
	respdata={}
	ob=Purchase_details.objects.filter(username=username,status="Approved")
	for j in ob:
		p_id=j.p_id
		username=j.username
		product_name=j.product_name
		quantity=j.quantity
		total_amount=j.total_amount
		status=j.status

		data={}
		data["p_id"]=p_id
		data["username"]=username
		data["product_name"]=product_name
		data["quantity"]=quantity
		data["total_amount"]=total_amount
		data["status"]=status

		resplist.append(data)
	print(resplist)

	respdata["data"]=resplist
	print(respdata)
	return JsonResponse(respdata,safe=False)

@csrf_exempt
def get_payment_status(request):

	username=request.POST.get("username")
	resplist=[]
	respdata={}
	ob=Purchase_details.objects.all()
	for j in ob:
		p_id=j.p_id
		username=j.username
		product_name=j.product_name
		quantity=j.quantity
		total_amount=j.total_amount
		status=j.status

		data={}
		data["p_id"]=p_id
		data["username"]=username
		data["product_name"]=product_name
		data["quantity"]=quantity
		data["total_amount"]=total_amount
		data["status"]=status

		resplist.append(data)
	print(resplist)

	respdata["data"]=resplist
	print(respdata)
	return JsonResponse(respdata,safe=False)


@csrf_exempt
def fetch_user_payment_status(request):

	username=request.POST.get("username")
	resplist=[]
	respdata={}
	ob=Purchase_details.objects.filter(username=username)
	for j in ob:
		p_id=j.p_id
		username=j.username
		product_name=j.product_name
		quantity=j.quantity
		total_amount=j.total_amount
		status=j.status

		data={}
		data["p_id"]=p_id
		data["username"]=username
		data["product_name"]=product_name
		data["quantity"]=quantity
		data["total_amount"]=total_amount
		data["status"]=status

		resplist.append(data)
	print(resplist)

	respdata["data"]=resplist
	print(respdata)
	return JsonResponse(respdata,safe=False)

# Get current location (latitude, longitude) using geocoder
def get_current_location():
	current_location = '0,0'
	try:
		# Using ipinfo.io to get approximate location based on IP address
		g = geocoder.ip('me')
		current_location = f"{g.latlng[0]},{g.latlng[1]}"
	except Exception as e:
		print(f"Error getting location: {e}")
	
	return current_location



def create_payment_token(payer_id, payer_location, timestamp,p_id, products):
	payment_data = {
		'payer_id': payer_id,
		'p_id': p_id,
		'payer_location': payer_location,
		'timestamp': timestamp,
		'products': products
	}

	payment_token = json.dumps(payment_data)
	return payment_token

def generate_identity_signature(private_key, data):
	signature = hmac.new(private_key.encode(), data.encode(), hashlib.sha256).hexdigest()
	return signature

def g_qr_code(data, filename):
	qr = qrcode.QRCode(
		version=1,
		error_correction=qrcode.constants.ERROR_CORRECT_L,
		box_size=10,
		border=4,
	)
	qr.add_data(data)
	qr.make(fit=True)

	img = qr.make_image(fill_color="black", back_color="white")
	img.save(filename)

@csrf_exempt
def generate_qr_code(request):
	p_id=request.POST.get("p_id")
	username=request.POST.get("username")

	obj30=Purchase_details.objects.get(p_id=int(p_id))
	get_uname=obj30.username

	obj1=Users.objects.get(username=get_uname)
	get_identity=obj1.email
	private_key=obj1._p_key
	payer_id=str(obj1.u_id)
	p_id=int(p_id)

	timestamp = datetime.datetime.now().strftime('%Y%m%d%H%M%S')

	payer_location= get_current_location() # Format: latitude,longitude


	products_query = Purchase_details.objects.filter(p_id=int(p_id))
	products = [{'product_name': str(product.product_name), 'quantity': product.quantity} for product in products_query]

	print(products)

	# Create payment token
	payment_token = create_payment_token(payer_id, payer_location, timestamp, p_id, products)

	# Generate identity-based signature
	identity_signature = generate_identity_signature(private_key, payment_token)


	print("------------------------------")
	print(identity_signature)

	# Combine payment token and identity-based signature
	signed_payment_token = f'{payment_token}|{identity_signature}'

	filename = f"{products[0]['product_name']}_qr_code.png"
	# Generate QR code
	g_qr_code(signed_payment_token,filename)

	print('QR code generated successfully.')

	return JsonResponse({'msg': 'QR code generated successfully in cwd'},safe=False)


def generate_identity_signature(private_key, data):
	signature = hmac.new(private_key.encode(), data.encode(), hashlib.sha256).hexdigest()
	return signature

def verify_identity_signature(public_key, data, signature):
	expected_signature = hmac.new(public_key.encode(), data.encode(), hashlib.sha256).hexdigest()

	print("Expected signature : ",expected_signature)
	print("Signature got : ",signature)
	aa=hmac.compare_digest(expected_signature, signature)
	return aa


def validate_payment_token(payment_data, merchant_location):
	payer_location = payment_data.get('payer_location')

	print("P--------ayer locatio n: ",payer_location)
	print("Merchant locatio n: ",merchant_location)

	# Calculate the distance between payer_location and merchant_location
	distance = geodesic(payer_location, merchant_location).meters

	print("Distance : ",distance)
	# Check if the distance is within the acceptable range (100 meters in this case)
	if distance <= 100:
		return True
	else:
		return False

def decode_qr_code(qr_data: str) -> Tuple[str, str]:

	payment_token, signature = qr_data.split('|')

	return payment_token, signature


def create_transaction_token(payment_token, merchant_location, total_amount):
	transaction_data = {
		'payment_token': payment_token,
		'merchant_location': merchant_location,
		'total_amount': total_amount,
	}

	transaction_token = json.dumps(transaction_data)
	return transaction_token


def encrypt_transaction_token(transaction_token, aes_key):

	cipher = AES.new(aes_key, AES.MODE_ECB)

	padded_bytes = pad(transaction_token.encode(), 16)

	encrypted_bytes = cipher.encrypt(padded_bytes)

	encrypted_data = base64.b64encode(encrypted_bytes).decode('utf-8')

	return encrypted_data


def decrypt_transaction_data(encrypted_transaction_token, aes_key):

	encrypted_data = base64.b64decode(encrypted_transaction_token)

	cipher = AES.new(aes_key, AES.MODE_ECB)

	decrypted_data = cipher.decrypt(encrypted_data)

	final_decrypted_data = unpad(decrypted_data, 16).decode('utf-8')

	return final_decrypted_data


def extract_payment_info(decrypted_data):
 
	payment_info = json.loads(decrypted_data)

	payment_token = payment_info.get('payment_token')
	merchant_location = payment_info.get('merchant_location')
	total_amount = payment_info.get('total_amount')

	return payment_token, merchant_location, total_amount

def extract_p_token(decrypted_data):
 
	payment_token_data = json.loads(decrypted_data)
	return payment_token_data



def verify_transaction(payment_token, merchant_location):

	payer_location = payment_token.get('payer_location')
	distance = geodesic(payer_location, merchant_location).meters
	return distance <= 100


def validate_timestamp(payment_token):

	payer_timestamp = payment_token.get('timestamp')
	current_time = datetime.datetime.now()
	timestamp_diff = current_time - datetime.datetime.strptime(payer_timestamp, '%Y%m%d%H%M%S')
	return timestamp_diff.total_seconds() <= 180


def payment_server(encrypted_transaction_token,p_id):

	# a) Perform AES Decryption to decode the transaction data
	decrypted_data = decrypt_transaction_data(encrypted_transaction_token, aes_key)

	print("decrypted_data : ",decrypted_data)

	if decrypted_data is not None:
		# Extract relevant information from decrypted data
		payment_token, merchant_location, total_amount = extract_payment_info(decrypted_data)

		payment_token_data=extract_p_token(payment_token)
		# b) Perform verification
		if verify_transaction(payment_token_data, merchant_location):
			# c) If verification is confirmed, the payment is successful
			if validate_timestamp(payment_token_data):
				print("\n\nPayment verification successful. Processing payment...")
				obj1=Purchase_details.objects.get(p_id=int(p_id))
				obj1.status="Success"
				obj1.save()

				resp='Payment verification successful.'
				return resp

			else:
				print("Invalid timestamp. Payment unsuccessful.")
				obj12=Purchase_details.objects.get(p_id=int(p_id))
				obj12.status="Failed"
				obj12.save()

				resp='Invalid timestamp. Payment unsuccessful.'
				return resp
		else:
			print("Verification failed. Payment unsuccessful.")
			obj121=Purchase_details.objects.get(p_id=int(p_id))
			obj121.status="Failed"
			obj121.save()
			resp='Verification failed. Payment unsuccessful.'
			return resp

	else:
		print("Decryption failed. Payment unsuccessful.")
		obj112=Purchase_details.objects.get(p_id=int(p_id))
		obj112.status="Failed"
		obj112.save()

		resp='Decryption failed. Payment unsuccessful.'
		return resp


@csrf_exempt
def verify_decode(request):
	global aes_key
	scanned_qr_code_data=request.POST.get("value")

	payment_token, identity_signature = decode_qr_code(scanned_qr_code_data)
	print(payment_token)
	payment_data = json.loads(payment_token)

	p_id = payment_data.get('p_id')

	obj1=Purchase_details.objects.get(p_id=int(p_id))
	payer_username=obj1.username
	total_amount=obj1.total_amount
	obj112=Users.objects.get(username=payer_username)
	payer_public_key=obj112._p_key
	merchant_location = get_current_location()
	# Verify identity-based signature
	if verify_identity_signature(payer_public_key, payment_token, identity_signature):
		print('Signature verification successful.')


		# Validate payment token
		if validate_payment_token(payment_data, merchant_location):
			# Continue processing the payment
			# Create transaction token
			transaction_token = create_transaction_token(payment_token, merchant_location, total_amount)

			print("******************")
			print('transaction_token :::',transaction_token)
			# Encrypt transaction token using AES

			aes_key = get_random_bytes(16)  # Replace with your key management logic
			encrypted_transaction_token = encrypt_transaction_token(transaction_token, aes_key)

			print("******************")
			print("Key : ",aes_key)
			print('encrypted transaction_token :::',encrypted_transaction_token)

			# Send encrypted transaction token to the payment server
			get_response=payment_server(encrypted_transaction_token,p_id)

			if get_response=="Payment verification successful.":
				return JsonResponse({'msg': 'Payment successful'},safe=False)
			else:
				return JsonResponse({'msg': 'Payment Failed'},safe=False)

		else:
			print('Invalid payment token. Payment validation failed.')

			obj12=Purchase_details.objects.get(p_id=int(p_id))
			obj12.status="Failed"
			obj12.save()
			return JsonResponse({'msg': 'Invalid payment token. Payment validation failed.'},safe=False)
	else:
		print('Signature verification failed. The QR code may have been tampered with.')
		obj12=Purchase_details.objects.get(p_id=int(p_id))
		obj12.status="Failed"
		obj12.save()
		return JsonResponse({'msg': 'Signature verification failed. The QR code may have been tampered with.'},safe=False)






