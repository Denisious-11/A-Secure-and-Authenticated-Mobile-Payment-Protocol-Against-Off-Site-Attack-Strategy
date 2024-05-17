# -*- coding: utf-8 -*-
# Generated by Django 1.11.17 on 2024-01-16 11:08
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('backend_app', '0005_auto_20240116_1453'),
    ]

    operations = [
        migrations.CreateModel(
            name='Users1',
            fields=[
                ('u_id', models.IntegerField(primary_key=True, serialize=False)),
                ('username', models.CharField(max_length=255)),
                ('password', models.CharField(max_length=255)),
                ('name', models.CharField(max_length=255)),
                ('address', models.CharField(max_length=255)),
                ('user_type', models.CharField(max_length=255)),
                ('email', models.CharField(max_length=255)),
                ('public_key', models.CharField(max_length=255)),
                ('private_key', models.CharField(max_length=255)),
            ],
        ),
        migrations.DeleteModel(
            name='Users',
        ),
    ]
