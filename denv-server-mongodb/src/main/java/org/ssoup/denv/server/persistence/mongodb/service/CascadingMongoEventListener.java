package org.ssoup.denv.server.persistence.mongodb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.ssoup.denv.server.persistence.mongodb.annotation.CascadeSave;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * From http://www.javacodegeeks.com/2013/11/spring-data-mongodb-cascade-save-on-dbref-objects.html
 */
@Component
public class CascadingMongoEventListener extends AbstractMongoEventListener {

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void onBeforeConvert(final Object source) {
        ReflectionUtils.doWithFields(source.getClass(), new ReflectionUtils.FieldCallback() {

            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                ReflectionUtils.makeAccessible(field);

                if (field.isAnnotationPresent(DBRef.class) && field.isAnnotationPresent(CascadeSave.class)) {
                    final Object fieldValue = field.get(source);

                    if (field.getType().isArray()) {
                        Object[] arrayFieldValue = (Object[])fieldValue;
                        for (Object arrayFieldItemValue : arrayFieldValue) {
                            saveFieldValue(arrayFieldItemValue);
                        }
                    } else if (Collection.class.isAssignableFrom((field.getType()))) {
                        Collection collectionFieldValue = (Collection)fieldValue;
                        for (Object collectionFieldItemValue : collectionFieldValue) {
                            saveFieldValue(collectionFieldItemValue);
                        }
                    } else {
                        saveFieldValue(fieldValue);
                    }
                }
            }

            private void saveFieldValue(Object fieldValue) {
                DbRefFieldCallback callback = new DbRefFieldCallback();

                ReflectionUtils.doWithFields(fieldValue.getClass(), callback);

                if (!callback.isIdFound()) {
                    throw new MappingException("Cannot perform cascade save on child object without id set");
                }

                mongoOperations.save(fieldValue);
            }
        });
    }

    private static class DbRefFieldCallback implements ReflectionUtils.FieldCallback {
        private boolean idFound;

        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
            ReflectionUtils.makeAccessible(field);

            if (field.isAnnotationPresent(Id.class)) {
                idFound = true;
            }
        }

        public boolean isIdFound() {
            return idFound;
        }
    }
}
