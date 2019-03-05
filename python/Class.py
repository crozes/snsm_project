# -*- coding: utf-8 -*
# On importe Tkinter
from tkinter import *
from tkinter import messagebox
from PIL import ImageTk, Image
import json
import os,sys

def getName(path):
    
    files=os.listdir(path)
    return files

class ItemList:

    def __init__(self):
        self.nom="nom"
        self.navigation="navigation"
        self.etat="etat"
        self.date="date"
        self.duree=6666
        self.data=data[65,5,6,8,9]


    def _get_nom(self):
        return self.nom
    def _get_navigation(self):
        return self.navigation
    def _get_etat(self):
        return self.etat
    def _get_date(self):
        return self.date
    def _get_data(self):
        return self.data

    def json_init(self,path):
        try:
            json_data=open(path,'r')
            data_dict=json.load(json_data)
            self.nom=data_dict["nom"]
            self.navigation=data_dict["navigation"]
            self.etat=data_dict["etat"]
            self.date=data_dict["date"]
            self.data=data_dict["data"]
            json_data.close()
            
        except ValueError:
            messagebox.showinfo("Alerte", "échec d'ouverture du fichier"+path)
            
