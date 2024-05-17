# -*- coding: utf-8 -*-
# Generated by Django 1.11.17 on 2024-01-15 08:05
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('backend_app', '0002_auto_20240113_1514'),
    ]

    operations = [
        migrations.CreateModel(
            name='Purchase_details',
            fields=[
                ('p_id', models.IntegerField(primary_key=True, serialize=False)),
                ('username', models.CharField(max_length=255)),
                ('product_name', models.CharField(max_length=255)),
                ('quantity', models.CharField(max_length=255)),
                ('total_amount', models.CharField(max_length=255)),
                ('status', models.CharField(default='Pending', max_length=255)),
            ],
        ),
    ]
