package com.bulunduc.shoppingcart.listeners;

import com.bulunduc.shoppingcart.models.Template;

public interface TemplateDialogClickListener {
    void onTemplateAddClick(Template template, int position);
    void onTemplateDeleteClick(int position);

}
