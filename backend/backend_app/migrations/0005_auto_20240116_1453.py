# -*- coding: utf-8 -*-
# Generated by Django 1.11.17 on 2024-01-16 09:23
from __future__ import unicode_literals

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('backend_app', '0004_auto_20240116_1453'),
    ]

    operations = [
        migrations.RenameModel(
            old_name='Users1',
            new_name='Users',
        ),
    ]